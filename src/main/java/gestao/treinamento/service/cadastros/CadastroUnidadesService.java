package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.UnidadeDTO;
import gestao.treinamento.model.entidades.*;
import gestao.treinamento.repository.cadastros.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CadastroUnidadesService {

    @Autowired
    private final CadastroUnidadesRepository repository;

    private final CadastroResponsavelTecnicoRepository responsavelTecnicoRepository;
    private final CadastroUnidadeResponsavelTecnicoRepository unidadeResponsavelTecnicoRepository;

    private final CadastroPerfilRepository perfilRepository;
    private final CadastroUnidadePerfilRepository unidadePerfilRepository;


    // GET: Buscar todos os unidades
    public List<UnidadeDTO> consultaCadastro() {
        List<Unidade> unidades = repository.findAll();

        return unidades.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo unidade
    @Transactional
    public UnidadeDTO criarUnidade(UnidadeDTO dto) {

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

        // Verificar se há um pefil vinculado no DTO
        if (dto.getIdPerfilVinculo() != null) {
            // Recuperar o responsavelTecnico pelo ID
            Perfil perfil = perfilRepository.findById(dto.getIdPerfilVinculo())
                    .orElseThrow(() -> new RuntimeException("ResponsavelTecnico não encontrado: ID " + dto.getIdPerfilVinculo()));

            // Criar a associação unidade-perfil
            UnidadePerfil unidadePerfil = new UnidadePerfil();
            unidadePerfil.setUnidade(unidade);
            unidadePerfil.setPerfil(perfil);

            // Salvar a associação
            unidadePerfilRepository.save(unidadePerfil);
        }

        //Retornar o DTO de Unidade criada
        return convertToDTO(unidade);
    }

    // PUT: Atualizar unidade existente
    @Transactional
    public UnidadeDTO atualizarUnidade(Long id, UnidadeDTO dto) {
        Unidade existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrado com ID: " + id));

        existente.setNome(dto.getNome());
//        existente.setGerenteResponsavel(unidade.getGerenteResponsavel());
//        existente.setResponsavelTecnico(unidade.getResponsavelTecnico());

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
        if (dto.getIdPerfilVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idUnidade e idPerfil)
            List<Long> idsPerfilsVinculadas = unidadePerfilRepository.findPerfilByUnidadeId(id);

            // Verificar se o perfil atual está na lista de associações
            Long idPerfilVinculo = dto.getIdPerfilVinculo();

            // Remover associações que não correspondem ao novo responsavelTecnico vinculado
            List<Long> idsParaRemover = idsPerfilsVinculadas.stream()
                    .filter(idResponsavelTecnico -> !idResponsavelTecnico.equals(idPerfilVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                unidadePerfilRepository.deleteByUnidadeIdAndPerfilIds(id, idsParaRemover);
            }

            // Verificar se o responsavelTecnico já está associado
            boolean existe = unidadePerfilRepository.existsByUnidadeIdAndPerfilId(id, idPerfilVinculo);
            if (!existe) {
                // Recuperar o responsavelTecnico pelo ID
                Perfil perfil = perfilRepository.findById(idPerfilVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("ResponsavelTecnico com ID " + idPerfilVinculo + " não encontrado"));

                // Criar a nova associação
                UnidadePerfil novaAssociacao = new UnidadePerfil();
                novaAssociacao.setUnidade(existente);
                novaAssociacao.setPerfil(perfil);

                // Salvar a nova associação
                unidadePerfilRepository.save(novaAssociacao);
            }
        }

        Unidade unidadeAtualizado = repository.save(existente);
        return convertToDTO(unidadeAtualizado);
    }

    public void deletarUnidade(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Unidade não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    public void deletarUnidades(List<Long> ids) {
        List<Unidade> unidades = repository.findAllById(ids);
        if (unidades.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
        }
        repository.deleteAll(unidades);
    }

    // Método auxiliar: Converter entidade para DTO
    private UnidadeDTO convertToDTO(Unidade unidade) {
        UnidadeDTO dto = new UnidadeDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dto.setId(unidade.getId());
        dto.setNome(unidade.getNome());

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

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Unidade convertToEntity(UnidadeDTO dto) {
        Unidade unidade = new Unidade();
        unidade.setNome(dto.getNome());

        return unidade;
    }
}
