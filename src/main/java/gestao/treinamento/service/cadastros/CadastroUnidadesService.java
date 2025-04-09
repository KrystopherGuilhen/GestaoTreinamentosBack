package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.UnidadeAssinaturaDTO;
import gestao.treinamento.model.dto.cadastros.UnidadeDTO;
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
public class CadastroUnidadesService {

    @Autowired
    private final CadastroUnidadesRepository repository;
    private final CadastroUnidadeAssinaturaRepository unidadeAssinaturaRepository;

    private final CadastroResponsavelTecnicoRepository responsavelTecnicoRepository;
    private final CadastroUnidadeResponsavelTecnicoRepository unidadeResponsavelTecnicoRepository;

    private final CadastroPerfilRepository perfilRepository;
    //private final CadastroUnidadePerfilRepository unidadePerfilRepository;


    // GET: Buscar todos os unidades
    public List<UnidadeDTO> consultaCadastro() {
        List<Unidade> unidades = repository.findAll();

        return unidades.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo unidade
    @Transactional
    public UnidadeDTO criarUnidade(UnidadeDTO dto) {
        // Validações obrigatórias
        if (!StringUtils.hasText(dto.getGerenteResponsavel())) {
            throw new IllegalArgumentException("Gerente Responsável é obrigatório");
        }
        if (!StringUtils.hasText(dto.getRazaoSocial())) {
            throw new IllegalArgumentException("Razão Social é obrigatória");
        }
        if (!StringUtils.hasText(dto.getCnpj())) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }

        // Validação de CNPJ único
        if (repository.existsByCnpj(dto.getCnpj())) {
            throw new DuplicateException("CNPJ já cadastrado: " + dto.getCnpj());
        }

        // Validação de tamanho dos campos
        if (dto.getGerenteResponsavel().length() > 100) {
            throw new IllegalArgumentException("Gerente Responsável deve ter até 100 caracteres");
        }
        if (dto.getRazaoSocial().length() > 255) {
            throw new IllegalArgumentException("Razão Social deve ter até 255 caracteres");
        }
        if (dto.getNomeFantasia() != null && dto.getNomeFantasia().length() > 255) {
            throw new IllegalArgumentException("Nome Fantasia deve ter até 255 caracteres");
        }

        // Converter o DTO para entidade unidade
        Unidade unidade = convertToEntity(dto);

        // Salvar o unidade e obter o ID gerado
        unidade = repository.save(unidade);

        // Verificar se há um responsavelTecnico vinculado no DTO
        if (dto.getIdResponsavelTecnicoVinculo() != null) {
            // Recuperar o responsavelTecnico pelo ID
            ResponsavelTecnico responsavelTecnico = responsavelTecnicoRepository.findById(dto.getIdResponsavelTecnicoVinculo())
                    .orElseThrow(() -> new RuntimeException("ResponsavelTecnico não encontrado: ID " + dto.getIdResponsavelTecnicoVinculo()));

            // Criar a associação unidade-responsavelTecnico
            UnidadeResponsavelTecnico unidadeResponsavelTecnico = new UnidadeResponsavelTecnico();
            unidadeResponsavelTecnico.setUnidade(unidade);
            unidadeResponsavelTecnico.setResponsavelTecnico(responsavelTecnico);

            // Salvar a associação
            unidadeResponsavelTecnicoRepository.save(unidadeResponsavelTecnico);
        }

//        // Verificar se há um pefil vinculado no DTO
//        if (dto.getIdPerfilVinculo() != null) {
//            // Recuperar o responsavelTecnico pelo ID
//            Perfil perfil = perfilRepository.findById(dto.getIdPerfilVinculo())
//                    .orElseThrow(() -> new RuntimeException("ResponsavelTecnico não encontrado: ID " + dto.getIdPerfilVinculo()));
//
//            // Criar a associação unidade-perfil
//            UnidadePerfil unidadePerfil = new UnidadePerfil();
//            unidadePerfil.setUnidade(unidade);
//            unidadePerfil.setPerfil(perfil);
//
//            // Salvar a associação
//            unidadePerfilRepository.save(unidadePerfil);
//        }

        //Retornar o DTO de Unidade criada
        return convertToDTO(unidade);
    }

    // PUT: Atualizar unidade existente
    @Transactional
    public UnidadeDTO atualizarUnidade(Long id, UnidadeDTO dto) {
        Unidade existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrado com ID: " + id));

        // Validações obrigatórias
        if (!StringUtils.hasText(dto.getGerenteResponsavel())) {
            throw new IllegalArgumentException("Gerente Responsável é obrigatório");
        }
        if (!StringUtils.hasText(dto.getRazaoSocial())) {
            throw new IllegalArgumentException("Razão Social é obrigatória");
        }

        // Validação de CNPJ (se foi alterado)
        if (StringUtils.hasText(dto.getCnpj())) {
            if (!dto.getCnpj().equals(existente.getCnpj())) {
                if (repository.existsByCnpjAndIdNot(dto.getCnpj(), id)) {
                    throw new DuplicateException("CNPJ já cadastrado: " + dto.getCnpj());
                }
                existente.setCnpj(dto.getCnpj());
            }
        }

        // Validação de tamanho
        if (dto.getGerenteResponsavel().length() > 100) {
            throw new IllegalArgumentException("Gerente Responsável deve ter até 100 caracteres");
        }
        if (dto.getRazaoSocial().length() > 255) {
            throw new IllegalArgumentException("Razão Social deve ter até 255 caracteres");
        }
        if (dto.getNomeFantasia() != null && dto.getNomeFantasia().length() > 255) {
            throw new IllegalArgumentException("Nome Fantasia deve ter até 255 caracteres");
        }


        existente.setNome(dto.getNome());
        existente.setGerenteResponsavel(dto.getGerenteResponsavel());
        existente.setRazaoSocial(dto.getRazaoSocial());
        existente.setNomeFantasia(dto.getNomeFantasia());
        existente.setCnpj(dto.getCnpj());
        existente.setEndereco(dto.getEndereco());
        existente.setCredenciamento(dto.getCredenciamento());

        // Atualizar associações com responsavelTecnico
        if (dto.getIdResponsavelTecnicoVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idUnidade e idResponsavelTecnico)
            List<Long> idsResponsavelTecnicosVinculadas = unidadeResponsavelTecnicoRepository.findResponsavelTecnicoByUnidadeId(id);

            // Verificar se o responsavelTecnico atual está na lista de associações
            Long idResponsavelTecnicoVinculo = dto.getIdResponsavelTecnicoVinculo();

            // Remover associações que não correspondem ao novo responsavelTecnico vinculado
            List<Long> idsParaRemover = idsResponsavelTecnicosVinculadas.stream()
                    .filter(idResponsavelTecnico -> !idResponsavelTecnico.equals(idResponsavelTecnicoVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                unidadeResponsavelTecnicoRepository.deleteByUnidadeIdAndResponsavelTecnicoIds(id, idsParaRemover);
            }

            // Verificar se o responsavelTecnico já está associado
            boolean existe = unidadeResponsavelTecnicoRepository.existsByUnidadeIdAndResponsavelTecnicoId(id, idResponsavelTecnicoVinculo);
            if (!existe) {
                // Recuperar o responsavelTecnico pelo ID
                ResponsavelTecnico responsavelTecnico = responsavelTecnicoRepository.findById(idResponsavelTecnicoVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("ResponsavelTecnico com ID " + idResponsavelTecnicoVinculo + " não encontrado"));

                // Criar a nova associação
                UnidadeResponsavelTecnico novaAssociacao = new UnidadeResponsavelTecnico();
                novaAssociacao.setUnidade(existente);
                novaAssociacao.setResponsavelTecnico(responsavelTecnico);

                // Salvar a nova associação
                unidadeResponsavelTecnicoRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com perfil
//        if (dto.getIdPerfilVinculo() != null) {
//            // Recuperar as associações existentes (com a chave composta idUnidade e idPerfil)
//            List<Long> idsPerfilsVinculadas = unidadePerfilRepository.findPerfilByUnidadeId(id);
//
//            // Verificar se o perfil atual está na lista de associações
//            Long idPerfilVinculo = dto.getIdPerfilVinculo();
//
//            // Remover associações que não correspondem ao novo responsavelTecnico vinculado
//            List<Long> idsParaRemover = idsPerfilsVinculadas.stream()
//                    .filter(idResponsavelTecnico -> !idResponsavelTecnico.equals(idPerfilVinculo))
//                    .toList();
//            if (!idsParaRemover.isEmpty()) {
//                unidadePerfilRepository.deleteByUnidadeIdAndPerfilIds(id, idsParaRemover);
//            }
//
//            // Verificar se o responsavelTecnico já está associado
//            boolean existe = unidadePerfilRepository.existsByUnidadeIdAndPerfilId(id, idPerfilVinculo);
//            if (!existe) {
//                // Recuperar o responsavelTecnico pelo ID
//                Perfil perfil = perfilRepository.findById(idPerfilVinculo)
//                        .orElseThrow(() -> new EntityNotFoundException("ResponsavelTecnico com ID " + idPerfilVinculo + " não encontrado"));
//
//                // Criar a nova associação
//                UnidadePerfil novaAssociacao = new UnidadePerfil();
//                novaAssociacao.setUnidade(existente);
//                novaAssociacao.setPerfil(perfil);
//
//                // Salvar a nova associação
//                unidadePerfilRepository.save(novaAssociacao);
//            }
//        }

        // 1. Atualizar assinatura (estratégia de substituição completa)
        if (dto.getAssinatura() != null) {
            // Remover assinatura existentes
            unidadeAssinaturaRepository.deleteByUnidadeId(id); // Método customizado no repository

            // Adicionar novas assinaturas
            List<UnidadeAssinatura> novasAssinaturas = dto.getAssinatura().stream()
                    .map(assinaturaDTO -> {
                        UnidadeAssinatura assinatura = new UnidadeAssinatura();
                        assinatura.setName(assinaturaDTO.getName());
                        assinatura.setMimeType(assinaturaDTO.getMimeType());
                        assinatura.setType(assinaturaDTO.getType());
                        assinatura.setSize(assinaturaDTO.getSize());

                        // Decodificar base64
                        String base64Data = assinaturaDTO.getBase64();
                        if (base64Data.contains(",")) {
                            base64Data = base64Data.split(",", 2)[1];
                        }
                        assinatura.setDados(Base64.getDecoder().decode(base64Data));

                        assinatura.setUnidade(existente);
                        return assinatura;
                    })
                    .collect(Collectors.toList());

            unidadeAssinaturaRepository.saveAll(novasAssinaturas);
        }

        Unidade unidadeAtualizado = repository.save(existente);
        return convertToDTO(unidadeAtualizado);
    }

    public void deletarUnidade(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Unidade com ID " + id + " não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Unidade não pode ser excluída pois está vinculada a outro cadastro");
        }
    }

    public void deletarUnidades(List<Long> ids) {
        List<Unidade> unidades = repository.findAllById(ids);
        if (unidades.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma unidade encontrado para os IDs fornecidos");
        }
        try {
            repository.deleteAll(unidades);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Uma ou mais unidades não podem ser excluídas pois estão vinculadas a outros cadastros.");
        }
    }

    // Método auxiliar: Converter entidade para DTO
    private UnidadeDTO convertToDTO(Unidade unidade) {
        UnidadeDTO dto = new UnidadeDTO();

        dto.setId(unidade.getId());
        dto.setNome(unidade.getNome());
        dto.setGerenteResponsavel(unidade.getGerenteResponsavel());
        dto.setRazaoSocial(unidade.getRazaoSocial());
        dto.setNomeFantasia(unidade.getNomeFantasia());
        dto.setCnpj(unidade.getCnpj());
        dto.setEndereco(unidade.getEndereco());
        dto.setCredenciamento(unidade.getCredenciamento());

        // Extrai o ID e nome do responsavelTecnico vinculado (único)
        if (unidade.getUnidadeResponsavelTecnicosVinculados() != null && !unidade.getUnidadeResponsavelTecnicosVinculados().isEmpty()) {
            UnidadeResponsavelTecnico unidadeResponsavelTecnico = unidade.getUnidadeResponsavelTecnicosVinculados().get(0);
            dto.setIdResponsavelTecnicoVinculo(unidadeResponsavelTecnico.getResponsavelTecnico().getId());

            List<String> nomeResponsavelTecnico = unidade.getUnidadeResponsavelTecnicosVinculados().stream()
                    .map(te -> te.getResponsavelTecnico().getNome())
                    .toList();
            dto.setNomeResponsavelTecnicoVinculo(nomeResponsavelTecnico);
        } else {
            dto.setIdResponsavelTecnicoVinculo(null);
            dto.setNomeResponsavelTecnicoVinculo(null);
        }

        // Converter assinatura para DTOs
        if (unidade.getAssinatura() != null) {
            List<UnidadeAssinaturaDTO> assinaturaDTO = unidade.getAssinatura().stream()
                    .map(assinatura -> {
                        UnidadeAssinaturaDTO assDTO = new UnidadeAssinaturaDTO();

                        // Mapear campos adicionais
                        assDTO.setName(assinatura.getName());
                        assDTO.setMimeType(assinatura.getMimeType());
                        assDTO.setType(assinatura.getType());
                        assDTO.setSize(assinatura.getSize());

                        // Apenas o Base64, sem o prefixo "data:"
                        assDTO.setBase64(Base64.getEncoder().encodeToString(assinatura.getDados()));

                        // ObjectURL geralmente não é persistido, mas se necessário:
                        assDTO.setObjectURL(assinatura.getObjectURL());

                        return assDTO;
                    })
                    .collect(Collectors.toList());
            dto.setAssinatura(assinaturaDTO);
        }

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Unidade convertToEntity(UnidadeDTO dto) {
        Unidade unidade = new Unidade();
        unidade.setNome(dto.getNome());
        unidade.setGerenteResponsavel(dto.getGerenteResponsavel());
        unidade.setRazaoSocial(dto.getRazaoSocial());
        unidade.setNomeFantasia(dto.getNomeFantasia());
        unidade.setCnpj(dto.getCnpj());
        unidade.setEndereco(dto.getEndereco());
        unidade.setCredenciamento(dto.getCredenciamento());

        // Processar assinatura (DTO → Entidade)
        if (dto.getAssinatura() != null) {
            List<UnidadeAssinatura> assinaturaEntidade = dto.getAssinatura().stream()
                    .map(assinaturaDTO -> {
                        UnidadeAssinatura assinatura = new UnidadeAssinatura();

                        // Mapear campos diretos
                        assinatura.setName(assinaturaDTO.getName());
                        assinatura.setMimeType(assinaturaDTO.getMimeType());
                        assinatura.setType(assinaturaDTO.getType());
                        assinatura.setSize(assinaturaDTO.getSize());

                        // Decodificar Base64 (já está pronto para byte[])
                        assinatura.setDados(Base64.getDecoder().decode(assinaturaDTO.getBase64()));

                        assinatura.setUnidade(unidade);
                        return assinatura;
                    })
                    .collect(Collectors.toList());
            unidade.setAssinatura(assinaturaEntidade);
        }

        return unidade;
    }
}
