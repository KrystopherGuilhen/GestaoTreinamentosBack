package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.PerfilDTO;
import gestao.treinamento.model.dto.cadastros.PerfilNivelPermissaoDTO;
import gestao.treinamento.model.dto.cadastros.PerfilNivelVisibilidadeDTO;
import gestao.treinamento.model.entidades.*;
import gestao.treinamento.repository.cadastros.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CadastroPerfilService {

    @Autowired
    private final CadastroPerfilRepository repository;

    private final CadastroUnidadesRepository unidadeRepository;
    private final CadastroPerfilUnidadeRepository perfilUnidadeRepository;

    private final CadastroNivelPermissaoRepository nivelPermissaoRepository;
    private final CadastroPerfilNivelPermissaoRepository perfilNivelPermissaoRepository;

    private final CadastroNivelVisibilidadeRepository nivelVisibilidadeRepository;
    private final CadastroPerfilNivelVisibilidadeRepository perfilNivelVisibilidadeRepository;

    // GET: Buscar todos os perfils
    public List<PerfilDTO> consultaCadastro() {
        List<Perfil> perfils = repository.findAll();

        return perfils.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo perfil
    @Transactional
    public PerfilDTO criarPerfil(PerfilDTO dto) {
        // Converter DTO para entidade Perfil
        Perfil perfil = convertToEntity(dto);

        // Salvar o perfil e obter o ID gerado
        perfil = repository.save(perfil);

        // Unidades vinculadas
        if (dto.getIdUnidadeVinculo() != null && dto.getIdUnidadeVinculo() != 0) {
            Unidade unidade = unidadeRepository.findById(dto.getIdUnidadeVinculo())
                    .orElseThrow(() -> new RuntimeException("Unidade não encontrada: ID " + dto.getIdUnidadeVinculo()));

            PerfilUnidade perfilUnidade = new PerfilUnidade();
            perfilUnidade.setPerfil(perfil);
            perfilUnidade.setUnidade(unidade);
            perfilUnidadeRepository.save(perfilUnidade);
        }

        // Processar as permissões
        if (dto.getPerfilNivelPermissaoDTO() != null) {
            for (PerfilNivelPermissaoDTO nivelPermissaoDTO : dto.getPerfilNivelPermissaoDTO()) {
                NivelPermissao nivelPermissao = nivelPermissaoRepository.findById(nivelPermissaoDTO.getIdPermissao())
                        .orElseThrow(() -> new RuntimeException("NivelPermissao não encontrado: ID " + nivelPermissaoDTO.getNomePermissao()));

                PerfilNivelPermissao perfilNivelPermissao = new PerfilNivelPermissao();
                perfilNivelPermissao.setPerfil(perfil);
                perfilNivelPermissao.setNivelPermissao(nivelPermissao);
                perfilNivelPermissaoRepository.save(perfilNivelPermissao);

                // Processar as visibilidades
                if (nivelPermissaoDTO.getPerfilNivelVisibilidadeDTO() != null) {
                    for (PerfilNivelVisibilidadeDTO visibilidadeDTO : nivelPermissaoDTO.getPerfilNivelVisibilidadeDTO()) {
                        NivelVisibilidade nivelVisibilidade = nivelVisibilidadeRepository.findById(visibilidadeDTO.getIdVisibilidade())
                                .orElseThrow(() -> new RuntimeException("NivelVisibilidade não encontrado: ID " + visibilidadeDTO.getIdVisibilidade()));

                        PerfilNivelVisibilidade perfilNivelVisibilidade = new PerfilNivelVisibilidade();
                        perfilNivelVisibilidade.setPerfil(perfil);
                        perfilNivelVisibilidade.setNivelVisibilidade(nivelVisibilidade);
                        perfilNivelVisibilidade.setNivelPermissao(nivelPermissao);
                        perfilNivelVisibilidadeRepository.save(perfilNivelVisibilidade);
                    }
                }
            }
        }

        return convertToDTO(perfil);
    }


    // PUT: Atualizar perfil existente
    @Transactional
    public PerfilDTO atualizarPerfil(Long id, PerfilDTO dto) {
        Perfil existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com ID: " + id));

        existente.setNome(dto.getNome());

        // Atualizar associações com unidade
        if (dto.getIdUnidadeVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idPerfil e idUnidade)
            List<Long> idsUnidadesVinculadas = perfilUnidadeRepository.findUnidadeByPerfilId(id);

            // Verificar se o unidade atual está na lista de associações
            Long idUnidadeVinculo = dto.getIdUnidadeVinculo();

            // Remover associações que não correspondem ao novo unidade vinculado
            List<Long> idsParaRemover = idsUnidadesVinculadas.stream()
                    .filter(idUnidade -> !idUnidade.equals(idUnidadeVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                perfilUnidadeRepository.deleteByPerfilIdAndUnidadeIds(id, idsParaRemover);
            }

            // Verificar se o unidade já está associado
            boolean existe = perfilUnidadeRepository.existsByPerfilIdAndUnidadeId(id, idUnidadeVinculo);
            if (!existe) {
                // Recuperar o unidade pelo ID
                Unidade unidade = unidadeRepository.findById(idUnidadeVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Unidade com ID " + idUnidadeVinculo + " não encontrado"));

                // Criar a nova associação
                PerfilUnidade novaAssociacao = new PerfilUnidade();
                novaAssociacao.setPerfil(existente);
                novaAssociacao.setUnidade(unidade);

                // Salvar a nova associação
                perfilUnidadeRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com nivelPermissao
        if (dto.getPerfilNivelPermissaoDTO() != null) {
            // Recuperar as associações existentes (com a chave composta idPerfil e idNivelPermissao)
            List<Long> idsNivelPermissaoVinculadas = perfilNivelPermissaoRepository.findNivelPermissaoByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsNivelPermissaoVinculadas.stream()
                    .filter(idNivelPermissao -> !dto.getPerfilNivelPermissaoDTO().stream()
                            .anyMatch(p -> p.getIdPermissao().equals(idNivelPermissao)))
                    .toList();
            perfilNivelPermissaoRepository.deleteByPerfilIdAndNivelPermissaoIds(id, idsParaRemover);

            // Adicionar novas associações (para cada nivelPermissao que não existe na tabela)
            for (PerfilNivelPermissaoDTO perfilNivelPermissaoDTO : dto.getPerfilNivelPermissaoDTO()) {
                Long idNivelPermissao = perfilNivelPermissaoDTO.getIdPermissao();

                boolean existe = perfilNivelPermissaoRepository.existsByPerfilIdAndNivelPermissaoId(id, idNivelPermissao);
                if (!existe) {
                    NivelPermissao nivelPermissao = nivelPermissaoRepository.findById(idNivelPermissao)
                            .orElseThrow(() -> new EntityNotFoundException("NivelPermissao com ID " + idNivelPermissao + " não encontrado"));

                    PerfilNivelPermissao novaAssociacao = new PerfilNivelPermissao();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setNivelPermissao(nivelPermissao);

                    // Salva a nova associação
                    perfilNivelPermissaoRepository.save(novaAssociacao);

                    // Atualizar as visibilidades dentro de cada nível de permissão
                    if (perfilNivelPermissaoDTO.getPerfilNivelVisibilidadeDTO() != null) {
                        for (PerfilNivelVisibilidadeDTO visibilidadeDTO : perfilNivelPermissaoDTO.getPerfilNivelVisibilidadeDTO()) {
                            NivelVisibilidade nivelVisibilidade = nivelVisibilidadeRepository.findById(visibilidadeDTO.getIdVisibilidade())
                                    .orElseThrow(() -> new EntityNotFoundException("NivelVisibilidade com ID " + visibilidadeDTO.getIdVisibilidade() + " não encontrado"));

                            // Agora, o nivelPermissao é corretamente definido no escopo
                            PerfilNivelVisibilidade novaVisibilidade = new PerfilNivelVisibilidade();
                            novaVisibilidade.setPerfil(existente);
                            novaVisibilidade.setNivelPermissao(nivelPermissao); // Associando com o NivelPermissao
                            novaVisibilidade.setNivelVisibilidade(nivelVisibilidade); // Associando com o NivelVisibilidade

                            // Salva a nova associação de visibilidade
                            perfilNivelVisibilidadeRepository.save(novaVisibilidade);
                        }
                    }
                }
            }
        }

        // Salvar o perfil atualizado no repositório
        Perfil perfilAtualizado = repository.save(existente);
        return convertToDTO(perfilAtualizado);
    }

    public void deletarPerfil(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Perfil não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    public void deletarPerfils(List<Long> ids) {
        List<Perfil> perfils = repository.findAllById(ids);
        if (perfils.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
        }
        repository.deleteAll(perfils);
    }

    // Método auxiliar: Converter entidade para DTO
    private PerfilDTO convertToDTO(Perfil perfil) {
        PerfilDTO dto = new PerfilDTO();

        dto.setId(perfil.getId());
        dto.setNome(perfil.getNome());

        // Unidades vinculadas
        if (perfil.getPerfilUnidadesVinculados() != null && !perfil.getPerfilUnidadesVinculados().isEmpty()) {
            PerfilUnidade perfilUnidade = perfil.getPerfilUnidadesVinculados().get(0);
            dto.setIdUnidadeVinculo(perfilUnidade.getUnidade().getId());

            List<String> nomeUnidade = perfil.getPerfilUnidadesVinculados().stream()
                    .map(te -> te.getUnidade().getNome())
                    .toList();
            dto.setNomeUnidadeVinculo(nomeUnidade);
        } else {
            dto.setIdUnidadeVinculo(null);
            dto.setNomeUnidadeVinculo(null);
        }

        // Níveis de Permissão e seus respectivos Níveis de Visibilidade
        List<PerfilNivelPermissaoDTO> nivelPermissaoDTOList = new ArrayList<>();
        for (PerfilNivelPermissao perfilNivelPermissao : perfil.getPerfilNiveisPermissoesVinculadas()) {
            PerfilNivelPermissaoDTO nivelPermissaoDTO = new PerfilNivelPermissaoDTO();
            nivelPermissaoDTO.setIdPermissao(perfilNivelPermissao.getNivelPermissao().getId());
            nivelPermissaoDTO.setNomePermissao(perfilNivelPermissao.getNivelPermissao().getNome());

            // Associar Níveis de Visibilidade para este Nível de Permissão
            List<PerfilNivelVisibilidadeDTO> visibilidadeDTOList = new ArrayList<>();
            for (PerfilNivelVisibilidade perfilNivelVisibilidade : perfilNivelPermissao.getPerfil().getPerfilNivelVisibilidadeVinculada()) {
                PerfilNivelVisibilidadeDTO visibilidadeDTO = new PerfilNivelVisibilidadeDTO();
                visibilidadeDTO.setIdVisibilidade(perfilNivelVisibilidade.getNivelVisibilidade().getId());
                visibilidadeDTO.setNomeVisibilidade(perfilNivelVisibilidade.getNivelVisibilidade().getNome());
                visibilidadeDTOList.add(visibilidadeDTO);
            }

            nivelPermissaoDTO.setPerfilNivelVisibilidadeDTO(visibilidadeDTOList);
            nivelPermissaoDTOList.add(nivelPermissaoDTO);
        }

        dto.setPerfilNivelPermissaoDTO(nivelPermissaoDTOList);

        return dto;
    }


    // Método auxiliar: Converter DTO para entidade
    private Perfil convertToEntity(PerfilDTO dto) {
        Perfil perfil = new Perfil();
        perfil.setNome(dto.getNome());

        return perfil;
    }
}