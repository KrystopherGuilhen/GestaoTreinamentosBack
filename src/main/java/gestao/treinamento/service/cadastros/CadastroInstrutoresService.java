package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.InstrutorDTO;
import gestao.treinamento.model.entidades.Instrutor;
import gestao.treinamento.model.entidades.InstrutorPessoa;
import gestao.treinamento.model.entidades.Pessoa;
import gestao.treinamento.repository.cadastros.CadastroInstrutorPessoaRepository;
import gestao.treinamento.repository.cadastros.CadastroInstrutoresRepository;
import gestao.treinamento.repository.cadastros.CadastroPessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class CadastroInstrutoresService {

    @Autowired
    private final CadastroInstrutoresRepository repository;

    private final CadastroPessoaRepository pessoaRepository;
    private final CadastroInstrutorPessoaRepository instrutorPessoaRepository;

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
        instrutor = repository.save(instrutor);

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
        existente.setFormacao(dto.getFormacao());
        existente.setNumeroRegistroProfissional(dto.getNumeroRegistroProfissional());
        existente.setUnidadeRegistroProfissional(dto.getUnidadeRegistroProfissional());
        existente.setEstadoRegistroProfissional(dto.getEstadoRegistroProfissional());

        // Atualizar associações com tipoPessoa (unicá)
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
        dto.setCertificado(instrutor.getCertificado());
        dto.setFormacao(instrutor.getFormacao());
        dto.setExperiencia(instrutor.getExperiencia());
        dto.setNumeroRegistroProfissional(instrutor.getNumeroRegistroProfissional());
        dto.setUnidadeRegistroProfissional(instrutor.getUnidadeRegistroProfissional());
        dto.setEstadoRegistroProfissional(instrutor.getEstadoRegistroProfissional());

        // Extrai o ID e nome do Tipo Pessoa vinculado (única)
        if (instrutor.getInstrutorPessoaVinculado() != null && !instrutor.getInstrutorPessoaVinculado().isEmpty()) {
            InstrutorPessoa instrutorPessoa = instrutor.getInstrutorPessoaVinculado().get(0);
            dto.setIdTipoPessoaVinculado(instrutorPessoa.getPessoa().getId());
            dto.setNomeTipoPessoaVinculado(instrutorPessoa.getPessoa().getNome());
        } else {
            dto.setIdTipoPessoaVinculado(null);
            dto.setNomeTipoPessoaVinculado(null);
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
        instrutor.setCertificado(dto.getCertificado());
        instrutor.setFormacao(dto.getFormacao());
        instrutor.setExperiencia(dto.getExperiencia());
        instrutor.setNumeroRegistroProfissional(dto.getNumeroRegistroProfissional());
        instrutor.setUnidadeRegistroProfissional(dto.getUnidadeRegistroProfissional());
        instrutor.setEstadoRegistroProfissional(dto.getEstadoRegistroProfissional());

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