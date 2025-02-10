package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.PerfilDTO;
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

        // Converter o DTO para entidade perfil
        Perfil perfil = convertToEntity(dto);

        // Verificar se há um unidade vinculado no DTO
        if (dto.getIdUnidadeVinculo() != null && dto.getIdUnidadeVinculo() != 0) {
            // Recuperar o unidade pelo ID
            Unidade unidade = unidadeRepository.findById(dto.getIdUnidadeVinculo())
                    .orElseThrow(() -> new RuntimeException("Unidade não encontrado: ID " + dto.getIdUnidadeVinculo()));

            // Criar a associação perfil-unidade
            PerfilUnidade perfilUnidade = new PerfilUnidade();
            perfilUnidade.setPerfil(perfil);
            perfilUnidade.setUnidade(unidade);

            // Salvar a associação
            perfilUnidadeRepository.save(perfilUnidade);
        }

        // Verificar se há nivelPermissao vinculadas no DTO
        if (dto.getIdNivelPermissaoVinculo() != null && !dto.getIdNivelPermissaoVinculo().isEmpty()) {
            for (Long idNivelPermissao : dto.getIdNivelPermissaoVinculo()) {
                // Recuperar o nivelPermissao pelo ID (se necessário)
                NivelPermissao nivelPermissao = nivelPermissaoRepository.findById(idNivelPermissao)
                        .orElseThrow(() -> new RuntimeException("Trabalhador não encontrada: ID " + idNivelPermissao));

                // Criar a associação perfil-nivelPermissao
                PerfilNivelPermissao perfilNivelPermissao = new PerfilNivelPermissao();
                perfilNivelPermissao.setPerfil(perfil);
                perfilNivelPermissao.setNivelPermissao(nivelPermissao);

                // Salvar a associação
                perfilNivelPermissaoRepository.save(perfilNivelPermissao);
            }
        }

        // Verificar se há nivelVisibilidade vinculadas no DTO
        if (dto.getIdNivelVisibilidadeVinculo() != null && !dto.getIdNivelVisibilidadeVinculo().isEmpty()) {
            for (Long idNivelVisibilidade : dto.getIdNivelVisibilidadeVinculo()) {
                // Recuperar o nivelVisibilidade pelo ID (se necessário)
                NivelVisibilidade nivelVisibilidade = nivelVisibilidadeRepository.findById(idNivelVisibilidade)
                        .orElseThrow(() -> new RuntimeException("Trabalhador não encontrada: ID " + idNivelVisibilidade));

                // Criar a associação perfil-nivelVisibilidade
                PerfilNivelVisibilidade perfilNivelVisibilidade = new PerfilNivelVisibilidade();
                perfilNivelVisibilidade.setPerfil(perfil);
                perfilNivelVisibilidade.setNivelVisibilidade(nivelVisibilidade);

                // Salvar a associação
                perfilNivelVisibilidadeRepository.save(perfilNivelVisibilidade);
            }
        }

        // Salvar o perfil e obter o ID gerado
        perfil = repository.save(perfil);

        //Retornar o DTO de Perfil criada
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
        if (dto.getIdNivelPermissaoVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idPerfil e idNivelPermissao)
            List<Long> idsNivelPermissaoVinculadas = perfilNivelPermissaoRepository.findNivelPermissaoByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsNivelPermissaoVinculadas.stream()
                    .filter(idNivelPermissao -> !dto.getIdNivelPermissaoVinculo().contains(idNivelPermissao))
                    .toList();
            perfilNivelPermissaoRepository.deleteByPerfilIdAndNivelPermissaoIds(id, idsParaRemover);

            // Adicionar novas associações (para cada nivelPermissao que não existe na tabela)
            for (Long idNivelPermissao : dto.getIdNivelPermissaoVinculo()) {
                boolean existe = perfilNivelPermissaoRepository.existsByPerfilIdAndNivelPermissaoId(id, idNivelPermissao);
                if (!existe) {
                    NivelPermissao nivelPermissao = nivelPermissaoRepository.findById(idNivelPermissao)
                            .orElseThrow(() -> new EntityNotFoundException("Trabalhador com ID " + idNivelPermissao + " não encontrada"));

                    PerfilNivelPermissao novaAssociacao = new PerfilNivelPermissao();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setNivelPermissao(nivelPermissao);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilNivelPermissaoRepository.save(novaAssociacao);
                }
            }
        }

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dto.setId(perfil.getId());
        dto.setNome(perfil.getNome());

        // Extrai o ID e nome do unidade vinculado (único)
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

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Perfil convertToEntity(PerfilDTO dto) {
        Perfil perfil = new Perfil();
        perfil.setNome(dto.getNome());

        return perfil;
    }
}