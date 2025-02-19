package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.InstrutorAssinaturaDTO;
import gestao.treinamento.model.dto.cadastros.InstrutorCertificadosDTO;
import gestao.treinamento.model.dto.cadastros.InstrutorDTO;
import gestao.treinamento.model.entidades.*;
import gestao.treinamento.repository.cadastros.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CadastroInstrutoresService {

    @Autowired
    private final CadastroInstrutoresRepository repository;
    private final CadastroPessoaRepository pessoaRepository;
    private final CadastroInstrutorFormacaoRepository instrutorFormacaoRepository;
    private final CadastroInstrutorPessoaRepository instrutorPessoaRepository;
    private final CadastroInstrutorCertificadosRepository instrutorCertificadosRepository;
    private final CadastroInstrutorAssinaturaRepository instrutorAssinaturaRepository;

    public List<InstrutorDTO> consultaCadastro() {
        List<Instrutor> instrutores = repository.findAll();

        return instrutores.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo Instrutor
    @Transactional
    public InstrutorDTO criarInstrutor(InstrutorDTO dto) {
        // Validação de CPF/CNPJ
        if (StringUtils.hasText(dto.getCpf())) {
            validateUniqueField("cpf", dto.getCpf());
        }
        if (StringUtils.hasText(dto.getCnpj())) {
            validateUniqueField("cnpj", dto.getCnpj());
        }

        // Converter o DTO para entidade Curso
        Instrutor instrutor = convertToEntity(dto);

        // Salvar o Instrutor e obter o ID gerado
        // Salvar o instrutor e criar uma referência final para uso no lambda
        final Instrutor instrutorSalvo = repository.save(instrutor);

        // Salva as formações individualmente
        if (dto.getFormacoes() != null && !dto.getFormacoes().isEmpty()) {
            List<InstrutorFormacao> formacoes = dto.getFormacoes().stream()
                    .map(nomeFormacao -> {
                        InstrutorFormacao formacaoEntity = new InstrutorFormacao();
                        formacaoEntity.setInstrutor(instrutorSalvo);
                        formacaoEntity.setFormacao(nomeFormacao);
                        return formacaoEntity;
                    })
                    .collect(Collectors.toList());
            instrutorFormacaoRepository.saveAll(formacoes);
        }

        // Verificar se há um pessoa vinculado no DTO
        if (dto.getIdTipoPessoaVinculado() != null) {
            // Recuperar o evento pelo ID
            Pessoa pessoa = pessoaRepository.findById(dto.getIdTipoPessoaVinculado())
                    .orElseThrow(() -> new RuntimeException("Tipo pessoa não encontrado: ID " + dto.getIdTipoPessoaVinculado()));

            // Criar a associação instrutor-pessoa
            InstrutorPessoa instrutorPessoa = new InstrutorPessoa();
            instrutorPessoa.setInstrutor(instrutor);
            instrutorPessoa.setPessoa(pessoa);

            // Salvar a associação
            instrutorPessoaRepository.save(instrutorPessoa);
        }

        // Retornar o DTO do Instrutor criado
        return convertToDTO(instrutor);
    }

    // PUT: Atualizar Instrutor existente
    @Transactional
    public InstrutorDTO atualizarInstrutor(Long id, InstrutorDTO dto) {
        Instrutor existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instrutor com ID " + id + " não encontrado"));

        existente.setId(dto.getId());
        existente.setNome(dto.getNome());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setTelefone(dto.getTelefone());
        existente.setCpf(dto.getCpf());
        existente.setCnpj(dto.getCnpj());
        existente.setEmail(dto.getEmail());
        existente.setPossuiMultiplasFormacoes(dto.isPossuiMultiplasFormacoes());
        existente.setExperiencia(dto.getExperiencia());
        existente.setNumeroRegistroProfissional(dto.getNumeroRegistroProfissional());
        existente.setUnidadeRegistroProfissional(dto.getUnidadeRegistroProfissional());
        existente.setEstadoRegistroProfissional(dto.getEstadoRegistroProfissional());

        // 🔴🔴🔴 Atualizar formações (nova lógica)
        if (dto.getFormacoes() != null) {
            // Remover formações existentes
            instrutorFormacaoRepository.deleteByInstrutorId(id);

            // Adicionar novas formações
            List<InstrutorFormacao> novasFormacoes = dto.getFormacoes().stream()
                    .map(nomeFormacao -> {
                        InstrutorFormacao formacao = new InstrutorFormacao();
                        formacao.setInstrutor(existente); // Usar a entidade existente
                        formacao.setFormacao(nomeFormacao);
                        return formacao;
                    })
                    .collect(Collectors.toList());
            instrutorFormacaoRepository.saveAll(novasFormacoes);
        }

        // 1. Atualizar certificados (estratégia de substituição completa)
        if (dto.getCertificados() != null) {
            // Remover certificados existentes
            instrutorCertificadosRepository.deleteByInstrutorId(id); // Método customizado no repository

            // Adicionar novos certificados
            List<InstrutorCertificados> novosCertificados = dto.getCertificados().stream()
                    .map(certDTO -> {
                        InstrutorCertificados certificado = new InstrutorCertificados();
                        certificado.setName(certDTO.getName());
                        certificado.setMimeType(certDTO.getMimeType());
                        certificado.setType(certDTO.getType());
                        certificado.setSize(certDTO.getSize());

                        // Decodificar base64
                        String base64Data = certDTO.getBase64();
                        if (base64Data.contains(",")) {
                            base64Data = base64Data.split(",", 2)[1];
                        }
                        certificado.setDados(Base64.getDecoder().decode(base64Data));

                        certificado.setInstrutor(existente);
                        return certificado;
                    })
                    .collect(Collectors.toList());

            instrutorCertificadosRepository.saveAll(novosCertificados);
        }

        // Atualizar associações com tipoPessoa (unico)
        if (dto.getIdTipoPessoaVinculado() != null) {
            // Recuperar as associações existentes (com a chave composta idPessoa e idPessoa)
            List<Long> idsPessoaVinculado = instrutorPessoaRepository.findPessoasByInstrutorId(id);

            // Verificar se a pessoa atual está na lista de associações
            Long idPessoaVinculo = dto.getIdTipoPessoaVinculado();

            // Remover associações que não correspondem ao nova pessoa vinculada
            List<Long> idsParaRemover = idsPessoaVinculado.stream()
                    .filter(idPessoa -> !idPessoa.equals(idPessoaVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                instrutorPessoaRepository.deleteByInstrutorIdAndPessoaIds(id, idsParaRemover);
            }

            // Verificar se a pessoa já está associado
            boolean existe = instrutorPessoaRepository.existsByInstrutorIdAndPessoaId(id, idPessoaVinculo);
            if (!existe) {
                // Recuperar a pessoa pelo ID
                Pessoa pessoa = pessoaRepository.findById(idPessoaVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Tipo Pessoa com ID " + idPessoaVinculo + " não encontrado"));

                // Criar a nova associação
                InstrutorPessoa novaAssociacao = new InstrutorPessoa();
                novaAssociacao.setInstrutor(existente);
                novaAssociacao.setPessoa(pessoa);

                // Salvar a nova associação
                instrutorPessoaRepository.save(novaAssociacao);
            }
        }

        // 1. Atualizar assinatura (estratégia de substituição completa)
        if (dto.getAssinatura() != null) {
            // Remover assinatura existentes
            instrutorAssinaturaRepository.deleteByInstrutorId(id); // Método customizado no repository

            // Adicionar novas assinaturas
            List<InstrutorAssinatura> novasAssinaturas = dto.getAssinatura().stream()
                    .map(certDTO -> {
                        InstrutorAssinatura assinatura = new InstrutorAssinatura();
                        assinatura.setName(certDTO.getName());
                        assinatura.setMimeType(certDTO.getMimeType());
                        assinatura.setType(certDTO.getType());
                        assinatura.setSize(certDTO.getSize());

                        // Decodificar base64
                        String base64Data = certDTO.getBase64();
                        if (base64Data.contains(",")) {
                            base64Data = base64Data.split(",", 2)[1];
                        }
                        assinatura.setDados(Base64.getDecoder().decode(base64Data));

                        assinatura.setInstrutor(existente);
                        return assinatura;
                    })
                    .collect(Collectors.toList());

            instrutorAssinaturaRepository.saveAll(novasAssinaturas);
        }

        Instrutor instrutorAtualizado = repository.save(existente);
        return convertToDTO(instrutorAtualizado);
    }

    // DELETE: Excluir Instrutor por ID
    public void deletarInstrutor(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Instrutor não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    // DELETE: Excluir múltiplos Instrutores por lista de IDs
    public void deletarInstrutores(List<Long> ids) {
        List<Instrutor> instrutores = repository.findAllById(ids);
        if (instrutores.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
        }
        repository.deleteAll(instrutores);
    }

    // Método auxiliar: Converter entidade para DTO
    private InstrutorDTO convertToDTO(Instrutor instrutor) {
        InstrutorDTO dto = new InstrutorDTO();

        dto.setId(instrutor.getId());
        dto.setNome(instrutor.getNome());
        dto.setCidade(instrutor.getCidade());
        dto.setEstado(instrutor.getEstado());
        dto.setTelefone(instrutor.getTelefone());
        dto.setCpf(instrutor.getCpf());
        dto.setCnpj(instrutor.getCnpj());
        dto.setEmail(instrutor.getEmail());
        dto.setPossuiMultiplasFormacoes(instrutor.isPossuiMultiplasFormacoes());
        dto.setExperiencia(instrutor.getExperiencia());
        dto.setNumeroRegistroProfissional(instrutor.getNumeroRegistroProfissional());
        dto.setUnidadeRegistroProfissional(instrutor.getUnidadeRegistroProfissional());
        dto.setEstadoRegistroProfissional(instrutor.getEstadoRegistroProfissional());

        // 🔴🔴🔴 Mapear formações da tabela InstrutorFormacao
        List<String> formacoes = instrutorFormacaoRepository.findByInstrutorId(instrutor.getId())
                .stream()
                .map(InstrutorFormacao::getFormacao)
                .collect(Collectors.toList());
        dto.setFormacoes(formacoes);

        // Converter certificados para DTOs
        if (instrutor.getCertificados() != null) {
            List<InstrutorCertificadosDTO> certificadosDTO = instrutor.getCertificados().stream()
                    .map(cert -> {
                        InstrutorCertificadosDTO certDTO = new InstrutorCertificadosDTO();

                        // Mapear campos adicionais
                        certDTO.setName(cert.getName());
                        certDTO.setMimeType(cert.getMimeType());
                        certDTO.setType(cert.getType());
                        certDTO.setSize(cert.getSize());

                        // Gerar Data URI para o front
                        certDTO.setBase64("data:" + cert.getMimeType() + ";base64," +
                                Base64.getEncoder().encodeToString(cert.getDados()));

                        // ObjectURL geralmente não é persistido, mas se necessário:
                        certDTO.setObjectURL(cert.getObjectURL());

                        return certDTO;
                    })
                    .collect(Collectors.toList());
            dto.setCertificados(certificadosDTO);
        }

        // Extrai o ID e nome do Tipo Pessoa vinculado (única)
        if (instrutor.getInstrutorPessoaVinculado() != null && !instrutor.getInstrutorPessoaVinculado().isEmpty()) {
            InstrutorPessoa instrutorPessoa = instrutor.getInstrutorPessoaVinculado().get(0);
            dto.setIdTipoPessoaVinculado(instrutorPessoa.getPessoa().getId());
            dto.setNomeTipoPessoaVinculado(instrutorPessoa.getPessoa().getNome());
        } else {
            dto.setIdTipoPessoaVinculado(null);
            dto.setNomeTipoPessoaVinculado(null);
        }

        // Converter assinatura para DTOs
        if (instrutor.getAssinatura() != null) {
            List<InstrutorAssinaturaDTO> assinaturaDTO = instrutor.getAssinatura().stream()
                    .map(cert -> {
                        InstrutorAssinaturaDTO assDTO = new InstrutorAssinaturaDTO();

                        // Mapear campos adicionais
                        assDTO.setName(cert.getName());
                        assDTO.setMimeType(cert.getMimeType());
                        assDTO.setType(cert.getType());
                        assDTO.setSize(cert.getSize());

                        // Gerar Data URI para o front
                        assDTO.setBase64("data:" + cert.getMimeType() + ";base64," +
                                Base64.getEncoder().encodeToString(cert.getDados()));

                        // ObjectURL geralmente não é persistido, mas se necessário:
                        assDTO.setObjectURL(cert.getObjectURL());

                        return assDTO;
                    })
                    .collect(Collectors.toList());
            dto.setAssinatura(assinaturaDTO);
        }

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Instrutor convertToEntity(InstrutorDTO dto) {
        Instrutor instrutor = new Instrutor();

        instrutor.setId(dto.getId());
        instrutor.setNome(dto.getNome());
        instrutor.setCidade(dto.getCidade());
        instrutor.setEstado(dto.getEstado());
        instrutor.setTelefone(dto.getTelefone());

        // Campos únicos opcionais (tratar vazios)
        instrutor.setCpf(StringUtils.hasText(dto.getCpf()) ? dto.getCpf() : null);
        instrutor.setCnpj(StringUtils.hasText(dto.getCnpj()) ? dto.getCnpj() : null);

        instrutor.setEmail(dto.getEmail());
        instrutor.setPossuiMultiplasFormacoes(dto.isPossuiMultiplasFormacoes());
        instrutor.setExperiencia(dto.getExperiencia());
        instrutor.setNumeroRegistroProfissional(dto.getNumeroRegistroProfissional());
        instrutor.setUnidadeRegistroProfissional(dto.getUnidadeRegistroProfissional());
        instrutor.setEstadoRegistroProfissional(dto.getEstadoRegistroProfissional());

        //        if (dto.getCertificados().getFirst().getBase64() == null || !dto.getCertificados().getFirst().getBase64().contains(",")) {
//            throw new IllegalArgumentException("Base64 inválido");
//        }

        // Processar certificados (DTO → Entidade)
        if (dto.getCertificados() != null) {
            List<InstrutorCertificados> certificadosEntidade = dto.getCertificados().stream()
                    .map(certificadosDTO -> {
                        InstrutorCertificados certificados = new InstrutorCertificados();

                        // Mapear campos diretos
                        certificados.setName(certificadosDTO.getName());
                        certificados.setMimeType(certificadosDTO.getMimeType());
                        certificados.setType(certificadosDTO.getType());
                        certificados.setSize(certificadosDTO.getSize());

                        // Decodificar Base64 (já está pronto para byte[])
                        certificados.setDados(Base64.getDecoder().decode(certificadosDTO.getBase64()));

                        certificados.setInstrutor(instrutor);
                        return certificados;
                    })
                    .collect(Collectors.toList());
            instrutor.setCertificados(certificadosEntidade);
        }

        // Processar assinatura (DTO → Entidade)
        if (dto.getAssinatura() != null) {
            List<InstrutorAssinatura> assinaturaEntidade = dto.getAssinatura().stream()
                    .map(assinaturaDTO -> {
                        InstrutorAssinatura assinatura = new InstrutorAssinatura();

                        // Mapear campos diretos
                        assinatura.setName(assinaturaDTO.getName());
                        assinatura.setMimeType(assinaturaDTO.getMimeType());
                        assinatura.setType(assinaturaDTO.getType());
                        assinatura.setSize(assinaturaDTO.getSize());

                        // Decodificar Base64 (já está pronto para byte[])
                        assinatura.setDados(Base64.getDecoder().decode(assinaturaDTO.getBase64()));

                        assinatura.setInstrutor(instrutor);
                        return assinatura;
                    })
                    .collect(Collectors.toList());
            instrutor.setAssinatura(assinaturaEntidade);
        }

        return instrutor;
    }

    private void validateUniqueField(String fieldName, String value) {
        if (repository.existsByField(fieldName, value)) {
            throw new DataIntegrityViolationException(
                    String.format("Já existe um instrutor com %s = '%s'", fieldName, value)
            );
        }
    }
}