package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.PerfilDTO;
import gestao.treinamento.model.entidades.*;
import gestao.treinamento.repository.cadastros.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final CadastroPermissaoCursoRespository permissaoCursoRespository;
    private final CadastroPerfilPermissaoCursoRepository perfilPermissaoCursoRepository;

    private final CadastroPermissaoEmpresaRespository permissaoEmpresaRespository;
    private final CadastroPerfilPermissaoEmpresaRepository perfilPermissaoEmpresaRepository;

    private final CadastroPermissaoResponsavelTecnicoRespository permissaoResponsavelTecnicoRespository;
    private final CadastroPerfilPermissaoResponsavelTecnicoRepository perfilPermissaoResponsavelTecnicoRepository;

    private final CadastroPermissaoUnidadeRespository permissaoUnidadeRespository;
    private final CadastroPerfilPermissaoUnidadeRepository perfilPermissaoUnidadeRepository;

    private final CadastroPermissaoTrabalhadorRespository permissaoTrabalhadorRespository;
    private final CadastroPerfilPermissaoTrabalhadorRepository perfilPermissaoTrabalhadorRepository;

    private final CadastroPermissaoPalestraRespository permissaoPalestraRespository;
    private final CadastroPerfilPermissaoPalestraRepository perfilPermissaoPalestraRepository;

    private final CadastroPermissaoTurmaRespository permissaoTurmaRespository;
    private final CadastroPerfilPermissaoTurmaRepository perfilPermissaoTurmaRepository;

    private final CadastroPermissaoPerfilRespository permissaoPerfilRespository;
    private final CadastroPerfilPermissaoPerfilRepository perfilPermissaoPerfilRepository;

    private final CadastroPermissaoInstrutorRespository permissaoInstrutorRespository;
    private final CadastroPerfilPermissaoInstrutorRepository perfilPermissaoInstrutorRepository;

//    private final CadastroNivelVisibilidadeRepository nivelVisibilidadeRepository;
//    private final CadastroPerfilNivelVisibilidadeRepository perfilNivelVisibilidadeRepository;

    private final PasswordEncoder passwordEncoder;

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

        // Verificar se há Permissoes vinculados no DTO
        if (dto.getIdPermissaoVinculo() != null && !dto.getIdPermissaoVinculo().isEmpty()) {
            for (Long idPermissao : dto.getIdPermissaoVinculo()) {
                // Recuperar o permissao pelo ID (se necessário)
                NivelPermissao nivelPermissao = nivelPermissaoRepository.findById(idPermissao)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissao));

                // Criar a associação turma-trabalhador
                PerfilNivelPermissao perfilNivelPermissao = new PerfilNivelPermissao();
                perfilNivelPermissao.setNivelPermissao(nivelPermissao);
                perfilNivelPermissao.setPerfil(perfil);

                // Salvar a associação
                perfilNivelPermissaoRepository.save(perfilNivelPermissao);
            }
        }

        // Verificar se há Cursos vinculados no DTO
        if (dto.getIdPermissaoCursoVinculo() != null && !dto.getIdPermissaoCursoVinculo().isEmpty()) {
            for (Long idPermissaoCurso : dto.getIdPermissaoCursoVinculo()) {
                // Recuperar o PermissaoCurso pelo ID (se necessário)
                PermissaoCurso permissaoCurso = permissaoCursoRespository.findById(idPermissaoCurso)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoCurso));

                // Criar a associação perfil-PermissaoCurso
                PerfilPermissaoCurso perfilPermissaoCurso = new PerfilPermissaoCurso();
                perfilPermissaoCurso.setPermissaoCurso(permissaoCurso);
                perfilPermissaoCurso.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoCursoRepository.save(perfilPermissaoCurso);
            }
        }

        // Verificar se há Permissoes vinculados no DTO
        if (dto.getIdPermissaoEmpresaVinculo() != null && !dto.getIdPermissaoEmpresaVinculo().isEmpty()) {
            for (Long idPermissaoEmpresa : dto.getIdPermissaoEmpresaVinculo()) {
                // Recuperar o PermissaoEmpresa pelo ID (se necessário)
                PermissaoEmpresa permissaoEmpresa = permissaoEmpresaRespository.findById(idPermissaoEmpresa)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoEmpresa));

                // Criar a associação perfil-PermissaoEmpresa
                PerfilPermissaoEmpresa perfilPermissaoEmpresa = new PerfilPermissaoEmpresa();
                perfilPermissaoEmpresa.setPermissaoEmpresa(permissaoEmpresa);
                perfilPermissaoEmpresa.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoEmpresaRepository.save(perfilPermissaoEmpresa);
            }
        }

        // Verificar se há ResponsavelTecnicos vinculados no DTO
        if (dto.getIdPermissaoResponsavelTecnicoVinculo() != null && !dto.getIdPermissaoResponsavelTecnicoVinculo().isEmpty()) {
            for (Long idPermissaoResponsavelTecnico : dto.getIdPermissaoResponsavelTecnicoVinculo()) {
                // Recuperar o PermissaoResponsavelTecnico pelo ID (se necessário)
                PermissaoResponsavelTecnico permissaoResponsavelTecnico = permissaoResponsavelTecnicoRespository.findById(idPermissaoResponsavelTecnico)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoResponsavelTecnico));

                // Criar a associação perfil-PermissaoResponsavelTecnico
                PerfilPermissaoResponsavelTecnico perfilPermissaoResponsavelTecnico = new PerfilPermissaoResponsavelTecnico();
                perfilPermissaoResponsavelTecnico.setPermissaoResponsavelTecnico(permissaoResponsavelTecnico);
                perfilPermissaoResponsavelTecnico.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoResponsavelTecnicoRepository.save(perfilPermissaoResponsavelTecnico);
            }
        }

        // Verificar se há Unidades vinculados no DTO
        if (dto.getIdPermissaoUnidadeVinculo() != null && !dto.getIdPermissaoUnidadeVinculo().isEmpty()) {
            for (Long idPermissaoUnidade : dto.getIdPermissaoUnidadeVinculo()) {
                // Recuperar o PermissaoUnidade pelo ID (se necessário)
                PermissaoUnidade permissaoUnidade = permissaoUnidadeRespository.findById(idPermissaoUnidade)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoUnidade));

                // Criar a associação perfil-PermissaoUnidade
                PerfilPermissaoUnidade perfilPermissaoUnidade = new PerfilPermissaoUnidade();
                perfilPermissaoUnidade.setPermissaoUnidade(permissaoUnidade);
                perfilPermissaoUnidade.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoUnidadeRepository.save(perfilPermissaoUnidade);
            }
        }

        // Verificar se há Trabalhadors vinculados no DTO
        if (dto.getIdPermissaoTrabalhadorVinculo() != null && !dto.getIdPermissaoTrabalhadorVinculo().isEmpty()) {
            for (Long idPermissaoTrabalhador : dto.getIdPermissaoTrabalhadorVinculo()) {
                // Recuperar o PermissaoTrabalhador pelo ID (se necessário)
                PermissaoTrabalhador permissaoTrabalhador = permissaoTrabalhadorRespository.findById(idPermissaoTrabalhador)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoTrabalhador));

                // Criar a associação perfil-PermissaoTrabalhador
                PerfilPermissaoTrabalhador perfilPermissaoTrabalhador = new PerfilPermissaoTrabalhador();
                perfilPermissaoTrabalhador.setPermissaoTrabalhador(permissaoTrabalhador);
                perfilPermissaoTrabalhador.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoTrabalhadorRepository.save(perfilPermissaoTrabalhador);
            }
        }

        // Verificar se há Palestras vinculados no DTO
        if (dto.getIdPermissaoPalestraVinculo() != null && !dto.getIdPermissaoPalestraVinculo().isEmpty()) {
            for (Long idPermissaoPalestra : dto.getIdPermissaoPalestraVinculo()) {
                // Recuperar o PermissaoPalestra pelo ID (se necessário)
                PermissaoPalestra permissaoPalestra = permissaoPalestraRespository.findById(idPermissaoPalestra)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoPalestra));

                // Criar a associação perfil-PermissaoPalestra
                PerfilPermissaoPalestra perfilPermissaoPalestra = new PerfilPermissaoPalestra();
                perfilPermissaoPalestra.setPermissaoPalestra(permissaoPalestra);
                perfilPermissaoPalestra.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoPalestraRepository.save(perfilPermissaoPalestra);
            }
        }

        // Verificar se há Turmas vinculados no DTO
        if (dto.getIdPermissaoTurmaVinculo() != null && !dto.getIdPermissaoTurmaVinculo().isEmpty()) {
            for (Long idPermissaoTurma : dto.getIdPermissaoTurmaVinculo()) {
                // Recuperar o PermissaoTurma pelo ID (se necessário)
                PermissaoTurma permissaoTurma = permissaoTurmaRespository.findById(idPermissaoTurma)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoTurma));

                // Criar a associação perfil-PermissaoTurma
                PerfilPermissaoTurma perfilPermissaoTurma = new PerfilPermissaoTurma();
                perfilPermissaoTurma.setPermissaoTurma(permissaoTurma);
                perfilPermissaoTurma.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoTurmaRepository.save(perfilPermissaoTurma);
            }
        }

        // Verificar se há Perfils vinculados no DTO
        if (dto.getIdPermissaoPerfilVinculo() != null && !dto.getIdPermissaoPerfilVinculo().isEmpty()) {
            for (Long idPermissaoPerfil : dto.getIdPermissaoPerfilVinculo()) {
                // Recuperar o PermissaoPerfil pelo ID (se necessário)
                PermissaoPerfil permissaoPerfil = permissaoPerfilRespository.findById(idPermissaoPerfil)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoPerfil));

                // Criar a associação perfil-PermissaoPerfil
                PerfilPermissaoPerfil perfilPermissaoPerfil = new PerfilPermissaoPerfil();
                perfilPermissaoPerfil.setPermissaoPerfil(permissaoPerfil);
                perfilPermissaoPerfil.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoPerfilRepository.save(perfilPermissaoPerfil);
            }
        }

        // Verificar se há Instrutors vinculados no DTO
        if (dto.getIdPermissaoInstrutorVinculo() != null && !dto.getIdPermissaoInstrutorVinculo().isEmpty()) {
            for (Long idPermissaoInstrutor : dto.getIdPermissaoInstrutorVinculo()) {
                // Recuperar o PermissaoInstrutor pelo ID (se necessário)
                PermissaoInstrutor permissaoInstrutor = permissaoInstrutorRespository.findById(idPermissaoInstrutor)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: ID " + idPermissaoInstrutor));

                // Criar a associação perfil-PermissaoInstrutor
                PerfilPermissaoInstrutor perfilPermissaoInstrutor = new PerfilPermissaoInstrutor();
                perfilPermissaoInstrutor.setPermissaoInstrutor(permissaoInstrutor);
                perfilPermissaoInstrutor.setPerfil(perfil);

                // Salvar a associação
                perfilPermissaoInstrutorRepository.save(perfilPermissaoInstrutor);
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
        existente.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(dto.getSenha())); // Atualiza a senha se fornecida
        }

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

        // Atualizar associações com NivelPermissaoes
        if (dto.getIdPermissaoPerfilVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idNivelPermissao)
            List<Long> idsNivelPermissaosVinculadas = perfilNivelPermissaoRepository.findNivelPermissaoByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsNivelPermissaosVinculadas.stream()
                    .filter(idNivelPermissao -> !dto.getIdPermissaoVinculo().contains(idNivelPermissao))
                    .toList();
            perfilNivelPermissaoRepository.deleteByPerfilIdAndNivelPermissaoIds(id, idsParaRemover);

            // Adicionar novas associações (para cada NivelPermissao que não existe na tabela)
            for (Long idNivelPermissao : dto.getIdPermissaoVinculo()) {
                boolean existe = perfilNivelPermissaoRepository.existsByPerfilIdAndNivelPermissaoId(id, idNivelPermissao);
                if (!existe) {
                    NivelPermissao NivelPermissao = nivelPermissaoRepository.findById(idNivelPermissao)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idNivelPermissao + " não encontrada"));

                    PerfilNivelPermissao novaAssociacao = new PerfilNivelPermissao();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setNivelPermissao(NivelPermissao);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilNivelPermissaoRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com PermissaoCursos
        if (dto.getIdPermissaoVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoCurso)
            List<Long> idsPermissaoCursosVinculadas = perfilPermissaoCursoRepository.findPermissaoCursoByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoCursosVinculadas.stream()
                    .filter(idPermissaoCurso -> !dto.getIdPermissaoCursoVinculo().contains(idPermissaoCurso))
                    .toList();
            perfilPermissaoCursoRepository.deleteByPerfilIdAndPermissaoCursoIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoCurso que não existe na tabela)
            for (Long idPermissaoCurso : dto.getIdPermissaoCursoVinculo()) {
                boolean existe = perfilPermissaoCursoRepository.existsByPerfilIdAndPermissaoCursoId(id, idPermissaoCurso);
                if (!existe) {
                    PermissaoCurso PermissaoCurso = permissaoCursoRespository.findById(idPermissaoCurso)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoCurso + " não encontrada"));

                    PerfilPermissaoCurso novaAssociacao = new PerfilPermissaoCurso();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoCurso(PermissaoCurso);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoCursoRepository.save(novaAssociacao);
                }
            }
        } else {
            // Se getIdPermissaoPerfilVinculo estiver vazio ou nulo, deletar todas as permissões associadas
            perfilPermissaoCursoRepository.deleteByPerfilId(id);
        }

        // Atualizar associações com PermissaoEmpresas
        if (dto.getIdPermissaoEmpresaVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoEmpresa)
            List<Long> idsPermissaoEmpresasVinculadas = perfilPermissaoEmpresaRepository.findPermissaoEmpresaByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoEmpresasVinculadas.stream()
                    .filter(idPermissaoEmpresa -> !dto.getIdPermissaoEmpresaVinculo().contains(idPermissaoEmpresa))
                    .toList();
            perfilPermissaoEmpresaRepository.deleteByPerfilIdAndPermissaoEmpresaIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoEmpresa que não existe na tabela)
            for (Long idPermissaoEmpresa : dto.getIdPermissaoEmpresaVinculo()) {
                boolean existe = perfilPermissaoEmpresaRepository.existsByPerfilIdAndPermissaoEmpresaId(id, idPermissaoEmpresa);
                if (!existe) {
                    PermissaoEmpresa PermissaoEmpresa = permissaoEmpresaRespository.findById(idPermissaoEmpresa)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoEmpresa + " não encontrada"));

                    PerfilPermissaoEmpresa novaAssociacao = new PerfilPermissaoEmpresa();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoEmpresa(PermissaoEmpresa);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoEmpresaRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com PermissaoResponsavelTecnicos
        if (dto.getIdPermissaoResponsavelTecnicoVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoResponsavelTecnico)
            List<Long> idsPermissaoResponsavelTecnicosVinculadas = perfilPermissaoResponsavelTecnicoRepository.findPermissaoResponsavelTecnicoByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoResponsavelTecnicosVinculadas.stream()
                    .filter(idPermissaoResponsavelTecnico -> !dto.getIdPermissaoResponsavelTecnicoVinculo().contains(idPermissaoResponsavelTecnico))
                    .toList();
            perfilPermissaoResponsavelTecnicoRepository.deleteByPerfilIdAndPermissaoResponsavelTecnicoIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoResponsavelTecnico que não existe na tabela)
            for (Long idPermissaoResponsavelTecnico : dto.getIdPermissaoResponsavelTecnicoVinculo()) {
                boolean existe = perfilPermissaoResponsavelTecnicoRepository.existsByPerfilIdAndPermissaoResponsavelTecnicoId(id, idPermissaoResponsavelTecnico);
                if (!existe) {
                    PermissaoResponsavelTecnico PermissaoResponsavelTecnico = permissaoResponsavelTecnicoRespository.findById(idPermissaoResponsavelTecnico)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoResponsavelTecnico + " não encontrada"));

                    PerfilPermissaoResponsavelTecnico novaAssociacao = new PerfilPermissaoResponsavelTecnico();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoResponsavelTecnico(PermissaoResponsavelTecnico);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoResponsavelTecnicoRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com PermissaoUnidades
        if (dto.getIdPermissaoUnidadeVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoUnidade)
            List<Long> idsPermissaoUnidadesVinculadas = perfilPermissaoUnidadeRepository.findPermissaoUnidadeByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoUnidadesVinculadas.stream()
                    .filter(idPermissaoUnidade -> !dto.getIdPermissaoUnidadeVinculo().contains(idPermissaoUnidade))
                    .toList();
            perfilPermissaoUnidadeRepository.deleteByPerfilIdAndPermissaoUnidadeIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoUnidade que não existe na tabela)
            for (Long idPermissaoUnidade : dto.getIdPermissaoUnidadeVinculo()) {
                boolean existe = perfilPermissaoUnidadeRepository.existsByPerfilIdAndPermissaoUnidadeId(id, idPermissaoUnidade);
                if (!existe) {
                    PermissaoUnidade PermissaoUnidade = permissaoUnidadeRespository.findById(idPermissaoUnidade)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoUnidade + " não encontrada"));

                    PerfilPermissaoUnidade novaAssociacao = new PerfilPermissaoUnidade();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoUnidade(PermissaoUnidade);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoUnidadeRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com PermissaoTrabalhadors
        if (dto.getIdPermissaoTrabalhadorVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoTrabalhador)
            List<Long> idsPermissaoTrabalhadorsVinculadas = perfilPermissaoTrabalhadorRepository.findPermissaoTrabalhadorByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoTrabalhadorsVinculadas.stream()
                    .filter(idPermissaoTrabalhador -> !dto.getIdPermissaoTrabalhadorVinculo().contains(idPermissaoTrabalhador))
                    .toList();
            perfilPermissaoTrabalhadorRepository.deleteByPerfilIdAndPermissaoTrabalhadorIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoTrabalhador que não existe na tabela)
            for (Long idPermissaoTrabalhador : dto.getIdPermissaoTrabalhadorVinculo()) {
                boolean existe = perfilPermissaoTrabalhadorRepository.existsByPerfilIdAndPermissaoTrabalhadorId(id, idPermissaoTrabalhador);
                if (!existe) {
                    PermissaoTrabalhador PermissaoTrabalhador = permissaoTrabalhadorRespository.findById(idPermissaoTrabalhador)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoTrabalhador + " não encontrada"));

                    PerfilPermissaoTrabalhador novaAssociacao = new PerfilPermissaoTrabalhador();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoTrabalhador(PermissaoTrabalhador);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoTrabalhadorRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com PermissaoPalestras
        if (dto.getIdPermissaoPalestraVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoPalestra)
            List<Long> idsPermissaoPalestrasVinculadas = perfilPermissaoPalestraRepository.findPermissaoPalestraByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoPalestrasVinculadas.stream()
                    .filter(idPermissaoPalestra -> !dto.getIdPermissaoPalestraVinculo().contains(idPermissaoPalestra))
                    .toList();
            perfilPermissaoPalestraRepository.deleteByPerfilIdAndPermissaoPalestraIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoPalestra que não existe na tabela)
            for (Long idPermissaoPalestra : dto.getIdPermissaoPalestraVinculo()) {
                boolean existe = perfilPermissaoPalestraRepository.existsByPerfilIdAndPermissaoPalestraId(id, idPermissaoPalestra);
                if (!existe) {
                    PermissaoPalestra PermissaoPalestra = permissaoPalestraRespository.findById(idPermissaoPalestra)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoPalestra + " não encontrada"));

                    PerfilPermissaoPalestra novaAssociacao = new PerfilPermissaoPalestra();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoPalestra(PermissaoPalestra);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoPalestraRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com PermissaoTurmas
        if (dto.getIdPermissaoTurmaVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoTurma)
            List<Long> idsPermissaoTurmasVinculadas = perfilPermissaoTurmaRepository.findPermissaoTurmaByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoTurmasVinculadas.stream()
                    .filter(idPermissaoTurma -> !dto.getIdPermissaoTurmaVinculo().contains(idPermissaoTurma))
                    .toList();
            perfilPermissaoTurmaRepository.deleteByPerfilIdAndPermissaoTurmaIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoTurma que não existe na tabela)
            for (Long idPermissaoTurma : dto.getIdPermissaoTurmaVinculo()) {
                boolean existe = perfilPermissaoTurmaRepository.existsByPerfilIdAndPermissaoTurmaId(id, idPermissaoTurma);
                if (!existe) {
                    PermissaoTurma PermissaoTurma = permissaoTurmaRespository.findById(idPermissaoTurma)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoTurma + " não encontrada"));

                    PerfilPermissaoTurma novaAssociacao = new PerfilPermissaoTurma();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoTurma(PermissaoTurma);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoTurmaRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com PermissaoPerfils
        if (dto.getIdPermissaoPerfilVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoPerfil)
            List<Long> idsPermissaoPerfilsVinculadas = perfilPermissaoPerfilRepository.findPermissaoPerfilByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoPerfilsVinculadas.stream()
                    .filter(idPermissaoPerfil -> !dto.getIdPermissaoPerfilVinculo().contains(idPermissaoPerfil))
                    .toList();
            perfilPermissaoPerfilRepository.deleteByPerfilIdAndPermissaoPerfilIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoPerfil que não existe na tabela)
            for (Long idPermissaoPerfil : dto.getIdPermissaoPerfilVinculo()) {
                boolean existe = perfilPermissaoPerfilRepository.existsByPerfilIdAndPermissaoPerfilId(id, idPermissaoPerfil);
                if (!existe) {
                    PermissaoPerfil PermissaoPerfil = permissaoPerfilRespository.findById(idPermissaoPerfil)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoPerfil + " não encontrada"));

                    PerfilPermissaoPerfil novaAssociacao = new PerfilPermissaoPerfil();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoPerfil(PermissaoPerfil);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoPerfilRepository.save(novaAssociacao);
                }
            }
        } else {
            // Se getIdPermissaoPerfilVinculo estiver vazio ou nulo, deletar todas as permissões associadas
            perfilPermissaoPerfilRepository.deleteByPerfilId(id);
        }

        // Atualizar associações com PermissaoInstrutors
        if (dto.getIdPermissaoInstrutorVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idPermissaoInstrutor)
            List<Long> idsPermissaoInstrutorsVinculadas = perfilPermissaoInstrutorRepository.findPermissaoInstrutorByPerfilId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsPermissaoInstrutorsVinculadas.stream()
                    .filter(idPermissaoInstrutor -> !dto.getIdPermissaoInstrutorVinculo().contains(idPermissaoInstrutor))
                    .toList();
            perfilPermissaoInstrutorRepository.deleteByPerfilIdAndPermissaoInstrutorIds(id, idsParaRemover);

            // Adicionar novas associações (para cada PermissaoInstrutor que não existe na tabela)
            for (Long idPermissaoInstrutor : dto.getIdPermissaoInstrutorVinculo()) {
                boolean existe = perfilPermissaoInstrutorRepository.existsByPerfilIdAndPermissaoInstrutorId(id, idPermissaoInstrutor);
                if (!existe) {
                    PermissaoInstrutor PermissaoInstrutor = permissaoInstrutorRespository.findById(idPermissaoInstrutor)
                            .orElseThrow(() -> new EntityNotFoundException("Permissão com ID " + idPermissaoInstrutor + " não encontrada"));

                    PerfilPermissaoInstrutor novaAssociacao = new PerfilPermissaoInstrutor();
                    novaAssociacao.setPerfil(existente);
                    novaAssociacao.setPermissaoInstrutor(PermissaoInstrutor);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    perfilPermissaoInstrutorRepository.save(novaAssociacao);
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
        dto.setEmail(perfil.getEmail());

        // Unidades vinculadas (Unico)
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

        // Extrai os IDs e nomes dos cursos vinculados (Multiplo)
        List<Long> idNivelPermissaoVinculo = perfil.getPerfilNiveisPermissoesVinculadas() != null
                ? perfil.getPerfilNiveisPermissoesVinculadas().stream()
                .map(te -> te.getNivelPermissao().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoVinculo(idNivelPermissaoVinculo);

        List<String> nomeNivelPermissao = perfil.getPerfilNiveisPermissoesVinculadas().stream()
                .map(te -> te.getNivelPermissao().getNome())
                .toList();
        dto.setNomePermissaoVinculo(nomeNivelPermissao);

        // Extrai os IDs e nomes dos cursos vinculados (Multiplo)
        List<Long> idPermissaoCurso = perfil.getPerfilPermissaoCursos() != null
                ? perfil.getPerfilPermissaoCursos().stream()
                .map(te -> te.getPermissaoCurso().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoCursoVinculo(idPermissaoCurso);

        List<String> nomePermissaoCurso = perfil.getPerfilPermissaoCursos().stream()
                .map(te -> te.getPermissaoCurso().getNome())
                .toList();
        dto.setNomePermissaoCursoVinculo(nomePermissaoCurso);

        // Extrai os IDs e nomes dos Empresas vinculados (Multiplo)
        List<Long> idPermissaoEmpresa = perfil.getPerfilPermissaoEmpresas() != null
                ? perfil.getPerfilPermissaoEmpresas().stream()
                .map(te -> te.getPermissaoEmpresa().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoEmpresaVinculo(idPermissaoEmpresa);

        List<String> nomePermissaoEmpresa = perfil.getPerfilPermissaoEmpresas().stream()
                .map(te -> te.getPermissaoEmpresa().getNome())
                .toList();
        dto.setNomePermissaoEmpresaVinculo(nomePermissaoEmpresa);

        // Extrai os IDs e nomes dos Instrutors vinculados (Multiplo)
        List<Long> idPermissaoInstrutor = perfil.getPerfilPermissaoInstrutores() != null
                ? perfil.getPerfilPermissaoInstrutores().stream()
                .map(te -> te.getPermissaoInstrutor().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoInstrutorVinculo(idPermissaoInstrutor);

        List<String> nomePermissaoInstrutor = perfil.getPerfilPermissaoInstrutores().stream()
                .map(te -> te.getPermissaoInstrutor().getNome())
                .toList();
        dto.setNomePermissaoInstrutorVinculo(nomePermissaoInstrutor);

        // Extrai os IDs e nomes dos Palestras vinculados (Multiplo)
        List<Long> idPermissaoPalestra = perfil.getPerfilPermissaoPalestras() != null
                ? perfil.getPerfilPermissaoPalestras().stream()
                .map(te -> te.getPermissaoPalestra().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoPalestraVinculo(idPermissaoPalestra);

        List<String> nomePermissaoPalestra = perfil.getPerfilPermissaoPalestras().stream()
                .map(te -> te.getPermissaoPalestra().getNome())
                .toList();
        dto.setNomePermissaoPalestraVinculo(nomePermissaoPalestra);

        // Extrai os IDs e nomes dos Perfis vinculados (Multiplo)
        List<Long> idPermissaoPerfil = perfil.getPerfilPermissaoPerfis() != null
                ? perfil.getPerfilPermissaoPerfis().stream()
                .map(te -> te.getPermissaoPerfil().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoPerfilVinculo(idPermissaoPerfil);

        List<String> nomePermissaoPerfil = perfil.getPerfilPermissaoPerfis().stream()
                .map(te -> te.getPermissaoPerfil().getNome())
                .toList();
        dto.setNomePermissaoPerfilVinculo(nomePermissaoPerfil);

        // Extrai os IDs e nomes dos Turmas vinculados (Multiplo)
        List<Long> idPermissaoTurma = perfil.getPerfilPermissaoTurmas() != null
                ? perfil.getPerfilPermissaoTurmas().stream()
                .map(te -> te.getPermissaoTurma().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoTurmaVinculo(idPermissaoTurma);

        List<String> nomePermissaoTurma = perfil.getPerfilPermissaoTurmas().stream()
                .map(te -> te.getPermissaoTurma().getNome())
                .toList();
        dto.setNomePermissaoTurmaVinculo(nomePermissaoTurma);

        // Extrai os IDs e nomes dos ResponsavelTecnicos vinculados (Multiplo)
        List<Long> idPermissaoResponsavelTecnico = perfil.getPerfilPermissaoResponsavelTecnicos() != null
                ? perfil.getPerfilPermissaoResponsavelTecnicos().stream()
                .map(te -> te.getPermissaoResponsavelTecnico().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoResponsavelTecnicoVinculo(idPermissaoResponsavelTecnico);

        List<String> nomePermissaoResponsavelTecnico = perfil.getPerfilPermissaoResponsavelTecnicos().stream()
                .map(te -> te.getPermissaoResponsavelTecnico().getNome())
                .toList();
        dto.setNomePermissaoResponsavelTecnicoVinculo(nomePermissaoResponsavelTecnico);

        // Extrai os IDs e nomes dos Trabalhadors vinculados (Multiplo)
        List<Long> idPermissaoTrabalhador = perfil.getPerfilPermissaoTrabalhadores() != null
                ? perfil.getPerfilPermissaoTrabalhadores().stream()
                .map(te -> te.getPermissaoTrabalhador().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoTrabalhadorVinculo(idPermissaoTrabalhador);

        List<String> nomePermissaoTrabalhador = perfil.getPerfilPermissaoTrabalhadores().stream()
                .map(te -> te.getPermissaoTrabalhador().getNome())
                .toList();
        dto.setNomePermissaoTrabalhadorVinculo(nomePermissaoTrabalhador);

        // Extrai os IDs e nomes dos Unidades vinculados (Multiplo)
        List<Long> idPermissaoUnidade = perfil.getPerfilPermissaoUnidades() != null
                ? perfil.getPerfilPermissaoUnidades().stream()
                .map(te -> te.getPermissaoUnidade().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdPermissaoUnidadeVinculo(idPermissaoUnidade);

        List<String> nomePermissaoUnidade = perfil.getPerfilPermissaoUnidades().stream()
                .map(te -> te.getPermissaoUnidade().getNome())
                .toList();
        dto.setNomePermissaoUnidadeVinculo(nomePermissaoUnidade);

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Perfil convertToEntity(PerfilDTO dto) {
        Perfil perfil = new Perfil();
        perfil.setNome(dto.getNome());
        perfil.setEmail(dto.getEmail());
        perfil.setSenha(passwordEncoder.encode(dto.getSenha()));

        return perfil;
    }

}