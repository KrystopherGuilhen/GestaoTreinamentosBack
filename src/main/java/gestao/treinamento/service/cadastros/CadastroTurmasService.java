package gestao.treinamento.service.cadastros;

import gestao.treinamento.model.dto.cadastros.TurmaDTO;
import gestao.treinamento.model.entidades.*;
import gestao.treinamento.repository.cadastros.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CadastroTurmasService {

    @Autowired
    private final CadastroTurmasRepository repository;

    private final CadastroEventosRepository eventosRepository;
    private final CadastroTurmaEventoRepository turmaEventoRepository;

    private final CadastroModalidadesRepository modalidadesRepository;
    private final CadastroTurmaModalidadeRepository turmaModalidadeRepository;

    private final CadastroTrabalhadoresRepository trabalhadoresRepository;
    private final CadastroTurmaTrabalhadorRepository turmaTrabalhadorRepository;

    private final CadastroInstrutoresRepository instrutoresRepository;
    private final CadastroTurmaInstrutorRepository turmaInstrutorRepository;

    private final CadastroEmpresasRepository empresasRepository;
    private final CadastroTurmaEmpresaRepository turmaEmpresaRepository;

    private final CadastroCursosRepository cursosRepository;
    private final CadastroTurmaCursoRepository turmaCursoRepository;

    private final CadastroPalestrasRepository palestrasRepository;
    private final CadastroTurmaPalestraRepository turmaPalestraRepository;

    private final CadastroUnidadesRepository unidadeRepository;
    private final CadastroTurmaUnidadeRepository turmaUnidadeRepository;

    private final CadastroInstrutorFormacaoRepository instrutorFormacaoRepository;
    private final CadastroTurmaInstrutorFormacaoRepository turmaInstrutorFormacaoRepository;


    // GET: Buscar todos os turmas
    public List<TurmaDTO> consultaCadastro() {
        List<Turma> turmas = repository.findAll();

        return turmas.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo turma
    @Transactional
    public TurmaDTO criarTurma(TurmaDTO dto) {
        // Converter o DTO para entidade turma
        Turma turma = convertToEntity(dto);

        // Salvar o turma e obter o ID gerado
        turma = repository.save(turma);

        // Verificar se há um evento vinculado no DTO
        if (dto.getIdEventoVinculo() != null) {
            // Recuperar o evento pelo ID
            Evento evento = eventosRepository.findById(dto.getIdEventoVinculo())
                    .orElseThrow(() -> new RuntimeException("Evento não encontrado: ID " + dto.getIdEventoVinculo()));

            // Criar a associação turma-evento
            TurmaEvento turmaEvento = new TurmaEvento();
            turmaEvento.setTurma(turma);
            turmaEvento.setEvento(evento);

            // Salvar a associação
            turmaEventoRepository.save(turmaEvento);
        }

        // Verificar se há um modalidade vinculado no DTO
        if (dto.getIdModalidadeVinculo() != null) {
            // Recuperar o evento pelo ID
            Modalidade modalidade = modalidadesRepository.findById(dto.getIdModalidadeVinculo())
                    .orElseThrow(() -> new RuntimeException("Evento não encontrado: ID " + dto.getIdModalidadeVinculo()));

            // Criar a associação turma-evento
            TurmaModalidade turmaModalidade = new TurmaModalidade();
            turmaModalidade.setTurma(turma);
            turmaModalidade.setModalidade(modalidade);

            // Salvar a associação
            turmaModalidadeRepository.save(turmaModalidade);
        }

        // Verificar se há trabalhadores vinculados no DTO
        if (dto.getIdTrabalhadorVinculo() != null && !dto.getIdTrabalhadorVinculo().isEmpty()) {
            for (Long idTrabalhador : dto.getIdTrabalhadorVinculo()) {
                // Recuperar o trabalhador pelo ID (se necessário)
                Trabalhador trabalhador = trabalhadoresRepository.findById(idTrabalhador)
                        .orElseThrow(() -> new RuntimeException("Trabalhador não encontrada: ID " + idTrabalhador));

                // Criar a associação turma-trabalhador
                TurmaTrabalhador turmaTrabalhadors = new TurmaTrabalhador();
                turmaTrabalhadors.setTurma(turma);
                turmaTrabalhadors.setTrabalhador(trabalhador);

                // Salvar a associação
                turmaTrabalhadorRepository.save(turmaTrabalhadors);
            }
        }

        // Verificar se há instrutores no DTO
        if (dto.getIdInstrutorVinculo() != null) {
            // Recuperar o evento pelo ID
            Instrutor instrutor = instrutoresRepository.findById(dto.getIdInstrutorVinculo())
                    .orElseThrow(() -> new RuntimeException("Instrutor não encontrado: ID " + dto.getIdInstrutorVinculo()));

            // Criar a associação turma-instrutor
            TurmaInstrutor turmaInstrutor = new TurmaInstrutor();
            turmaInstrutor.setTurma(turma);
            turmaInstrutor.setInstrutor(instrutor);

            if (dto.getMultiplosInstrutores() == true) {
                turmaInstrutor.setIdMultiploInstrutor(1);
            } else {
                turmaInstrutor.setIdMultiploInstrutor(1);
            }

            // Salvar a associação
            turmaInstrutorRepository.save(turmaInstrutor);
        }

        if (dto.getIdInstrutorVinculoDois() != null) {
            // Recuperar o evento pelo ID
            Instrutor instrutor = instrutoresRepository.findById(dto.getIdInstrutorVinculoDois())
                    .orElseThrow(() -> new RuntimeException("Instrutor não encontrado: ID " + dto.getIdInstrutorVinculoDois()));

            // Criar a associação turma-instrutorUm
            TurmaInstrutor turmaInstrutor = new TurmaInstrutor();
            turmaInstrutor.setTurma(turma);
            turmaInstrutor.setInstrutor(instrutor);
            turmaInstrutor.setIdMultiploInstrutor(2);

            // Salvar a associação
            turmaInstrutorRepository.save(turmaInstrutor);
        }

        if (dto.getIdInstrutorVinculoDois() != null) {
            // Recuperar o evento pelo ID
            Instrutor instrutor = instrutoresRepository.findById(dto.getIdInstrutorVinculoDois())
                    .orElseThrow(() -> new RuntimeException("Instrutor não encontrado: ID " + dto.getIdInstrutorVinculoDois()));

            // Criar a associação turma-instrutorDois
            TurmaInstrutor turmaInstrutor = new TurmaInstrutor();
            turmaInstrutor.setTurma(turma);
            turmaInstrutor.setInstrutor(instrutor);
            turmaInstrutor.setIdMultiploInstrutor(3);

            // Salvar a associação
            turmaInstrutorRepository.save(turmaInstrutor);
        }

        // Verificar se há empresas vinculadas no DTO
        if (dto.getIdEmpresaVinculo() != null && !dto.getIdEmpresaVinculo().isEmpty()) {
            for (Long idEmpresa : dto.getIdEmpresaVinculo()) {
                // Recuperar o empresa pelo ID (se necessário)
                Empresa empresa = empresasRepository.findById(idEmpresa)
                        .orElseThrow(() -> new RuntimeException("Empresa não encontrada: ID " + idEmpresa));

                // Criar a associação turma-empresa
                TurmaEmpresa turmaEmpresas = new TurmaEmpresa();
                turmaEmpresas.setTurma(turma);
                turmaEmpresas.setEmpresa(empresa);

                // Salvar a associação
                turmaEmpresaRepository.save(turmaEmpresas);
            }
        }

        // Verificar se há um curso vinculado no DTO
        if (dto.getIdCursoVinculo() != null) {
            // Recuperar o evento pelo ID
            Curso curso = cursosRepository.findById(dto.getIdCursoVinculo())
                    .orElseThrow(() -> new RuntimeException("Evento não encontrado: ID " + dto.getIdCursoVinculo()));

            // Criar a associação turma-curso
            TurmaCurso turmaCurso = new TurmaCurso();
            turmaCurso.setTurma(turma);
            turmaCurso.setCurso(curso);

            // Salvar a associação
            turmaCursoRepository.save(turmaCurso);
        }

        // Verificar se há uma palestra vinculado no DTO
        if (dto.getIdPalestraVinculo() != null) {
            // Recuperar a palestra pelo ID
            Palestra palestra = palestrasRepository.findById(dto.getIdPalestraVinculo())
                    .orElseThrow(() -> new RuntimeException("Palestra não encontrado: ID " + dto.getIdPalestraVinculo()));

            // Criar a associação turma-curso
            TurmaPalestra turmaPalestra = new TurmaPalestra();
            turmaPalestra.setTurma(turma);
            turmaPalestra.setPalestra(palestra);

            // Salvar a associação
            turmaPalestraRepository.save(turmaPalestra);
        }

        // Verificar se há uma unidade vinculado no DTO
        if (dto.getIdUnidadeVinculo() != null) {
            // Recuperar o unidade pelo ID
            Unidade unidade = unidadeRepository.findById(dto.getIdUnidadeVinculo())
                    .orElseThrow(() -> new RuntimeException("Unidade não encontrada: ID " + dto.getIdUnidadeVinculo()));

            // Criar a associação turma-unidade
            TurmaUnidade turmaUnidade = new TurmaUnidade();
            turmaUnidade.setTurma(turma);
            turmaUnidade.setUnidade(unidade);

            // Salvar a associação
            turmaUnidadeRepository.save(turmaUnidade);
        }

        // Verificar se há um instrutorFormacao vinculado no DTO
        if (dto.getIdInstrutorFormacaoVinculo() != null) {
            // Recuperar o evento pelo ID
            InstrutorFormacao instrutorFormacao = instrutorFormacaoRepository.findById(dto.getIdInstrutorFormacaoVinculo())
                    .orElseThrow(() -> new RuntimeException("Formação não encontrado: ID " + dto.getIdInstrutorFormacaoVinculo()));

            // Criar a associação turma-instrutor-formacao
            TurmaInstrutorFormacao turmaInstrutorFormacao = new TurmaInstrutorFormacao();
            turmaInstrutorFormacao.setTurma(turma);
            turmaInstrutorFormacao.setInstrutorFormacao(instrutorFormacao);

            if (dto.getMultiplosInstrutores() == true) {
                turmaInstrutorFormacao.setIdMultiploFormacao(1);
            } else {
                turmaInstrutorFormacao.setIdMultiploFormacao(1);
            }

            // Salvar a associação
            turmaInstrutorFormacaoRepository.save(turmaInstrutorFormacao);
        }

        if (dto.getIdInstrutorFormacaoVinculoUm() != null) {
            // Recuperar o evento pelo ID
            InstrutorFormacao instrutorFormacao = instrutorFormacaoRepository.findById(dto.getIdInstrutorFormacaoVinculoUm())
                    .orElseThrow(() -> new RuntimeException("Formação não encontrado: ID " + dto.getIdInstrutorFormacaoVinculoUm()));

            // Criar a associação turma-instrutor-formacao
            TurmaInstrutorFormacao turmaInstrutorFormacao = new TurmaInstrutorFormacao();
            turmaInstrutorFormacao.setTurma(turma);
            turmaInstrutorFormacao.setInstrutorFormacao(instrutorFormacao);
            turmaInstrutorFormacao.setIdMultiploFormacao(2);

            // Salvar a associação
            turmaInstrutorFormacaoRepository.save(turmaInstrutorFormacao);
        }

        if (dto.getIdInstrutorFormacaoVinculoDois() != null) {
            // Recuperar o evento pelo ID
            InstrutorFormacao instrutorFormacao = instrutorFormacaoRepository.findById(dto.getIdInstrutorFormacaoVinculoDois())
                    .orElseThrow(() -> new RuntimeException("Formação não encontrado: ID " + dto.getIdInstrutorFormacaoVinculoDois()));

            // Criar a associação turma-instrutor-formacao
            TurmaInstrutorFormacao turmaInstrutorFormacao = new TurmaInstrutorFormacao();
            turmaInstrutorFormacao.setTurma(turma);
            turmaInstrutorFormacao.setInstrutorFormacao(instrutorFormacao);
            turmaInstrutorFormacao.setIdMultiploFormacao(3);

            // Salvar a associação
            turmaInstrutorFormacaoRepository.save(turmaInstrutorFormacao);
        }

        // Retornar o DTO do turma criado
        return convertToDTO(turma);
    }

    // PUT: Atualizar turma existente
    @Transactional
    public TurmaDTO atualizarTurma(Long id, TurmaDTO dto) {
        Turma existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma com ID " + id + " não encontrado"));

        existente.setNome(dto.getNome());
        existente.setAtivo(dto.getAtivo());

        if (dto.getDataInicio() != null) {
            String dataInicioStr = dto.getDataInicio();
            LocalDate dataInicio = null;

            try {
                // Primeiro, tenta converter do formato ISO 8601
                if (dataInicioStr.contains("T")) {
                    dataInicio = LocalDate.parse(dataInicioStr.split("T")[0]);
                } else {
                    // Tenta converter do formato dd/MM/yyyy
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dataInicio = LocalDate.parse(dataInicioStr, formatter);
                }
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de data inválido: " + dataInicioStr);
            }

            existente.setDataInicio(dataInicio);
        }

        if (dto.getDataFim() != null) {
            String dataFimStr = dto.getDataFim();
            LocalDate dataFim = null;

            try {
                // Primeiro, tenta converter do formato ISO 8601
                if (dataFimStr.contains("T")) {
                    dataFim = LocalDate.parse(dataFimStr.split("T")[0]);
                } else {
                    // Tenta converter do formato dd/MM/yyyy
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dataFim = LocalDate.parse(dataFimStr, formatter);
                }
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de data inválido: " + dataFimStr);
            }

            existente.setDataFim(dataFim);
        }

        existente.setValorContratoCrm(dto.getValorContratoCrm());
        existente.setNumeroContratoCrm(dto.getNumeroContratoCrm());
        existente.setIdCidadeTreinamento(dto.getIdCidadeTreinamento());
        existente.setNomeCidadeTreinamento(dto.getNomeCidadeTreinamento());
        existente.setObservacaoNr(dto.getObservacaoNr());
        existente.setMultiplosInstrutores(dto.getMultiplosInstrutores());

        // Atualizar associações com evento
        if (dto.getIdEventoVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idEvento)
            List<Long> idsEventosVinculadas = turmaEventoRepository.findEventoByTurmaId(id);

            // Verificar se o evento atual está na lista de associações
            Long idEventoVinculo = dto.getIdEventoVinculo();

            // Remover associações que não correspondem ao novo evento vinculado
            List<Long> idsParaRemover = idsEventosVinculadas.stream()
                    .filter(idEvento -> !idEvento.equals(idEventoVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                turmaEventoRepository.deleteByTurmaIdAndEventoIds(id, idsParaRemover);
            }

            // Verificar se o evento já está associado
            boolean existe = turmaEventoRepository.existsByTurmaIdAndEventoId(id, idEventoVinculo);
            if (!existe) {
                // Recuperar o evento pelo ID
                Evento evento = eventosRepository.findById(idEventoVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Evento com ID " + idEventoVinculo + " não encontrado"));

                // Criar a nova associação
                TurmaEvento novaAssociacao = new TurmaEvento();
                novaAssociacao.setTurma(existente);
                novaAssociacao.setEvento(evento);

                // Salvar a nova associação
                turmaEventoRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com modalidade
        if (dto.getIdModalidadeVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idModalidade)
            List<Long> idsModalidadesVinculadas = turmaModalidadeRepository.findModalidadeByTurmaId(id);

            // Verificar se o modalidade atual está na lista de associações
            Long idModalidadeVinculo = dto.getIdModalidadeVinculo();

            // Remover associações que não correspondem ao novo modalidade vinculado
            List<Long> idsParaRemover = idsModalidadesVinculadas.stream()
                    .filter(idModalidade -> !idModalidade.equals(idModalidadeVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                turmaModalidadeRepository.deleteByTurmaIdAndModalidadeIds(id, idsParaRemover);
            }

            // Verificar se o modalidade já está associado
            boolean existe = turmaModalidadeRepository.existsByTurmaIdAndModalidadeId(id, idModalidadeVinculo);
            if (!existe) {
                // Recuperar o modalidade pelo ID
                Modalidade modalidade = modalidadesRepository.findById(idModalidadeVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Modalidade com ID " + idModalidadeVinculo + " não encontrado"));

                // Criar a nova associação
                TurmaModalidade novaAssociacao = new TurmaModalidade();
                novaAssociacao.setTurma(existente);
                novaAssociacao.setModalidade(modalidade);

                // Salvar a nova associação
                turmaModalidadeRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com trabalhadores
        if (dto.getIdTrabalhadorVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idTrabalhador)
            List<Long> idsTrabalhadorsVinculadas = turmaTrabalhadorRepository.findTrabalhadorByTurmaId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsTrabalhadorsVinculadas.stream()
                    .filter(idTrabalhador -> !dto.getIdTrabalhadorVinculo().contains(idTrabalhador))
                    .toList();
            turmaTrabalhadorRepository.deleteByTurmaIdAndTrabalhadorIds(id, idsParaRemover);

            // Adicionar novas associações (para cada trabalhador que não existe na tabela)
            for (Long idTrabalhador : dto.getIdTrabalhadorVinculo()) {
                boolean existe = turmaTrabalhadorRepository.existsByTurmaIdAndTrabalhadorId(id, idTrabalhador);
                if (!existe) {
                    Trabalhador trabalhador = trabalhadoresRepository.findById(idTrabalhador)
                            .orElseThrow(() -> new EntityNotFoundException("Trabalhador com ID " + idTrabalhador + " não encontrada"));

                    TurmaTrabalhador novaAssociacao = new TurmaTrabalhador();
                    novaAssociacao.setTurma(existente);
                    novaAssociacao.setTrabalhador(trabalhador);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    turmaTrabalhadorRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com instrutorFormacao
        // Vínculo 1 (sempre atualiza se o ID estiver presente)
        if (dto.getIdInstrutorFormacaoVinculo() != null) {
            atualizarVinculoInstrutorFormacao(id, dto.getIdInstrutorFormacaoVinculo(), 1, existente);
        }

        // Sempre processar vínculos 2 e 3, mesmo com multiplosInstrutores = false
        if (dto.getMultiplosInstrutores()) {
            // Vínculo 2 (atualiza ou remove)
            if (dto.getIdInstrutorVinculoUm() != null) {
                atualizarVinculoInstrutor(id, dto.getIdInstrutorVinculoUm(), 2, existente);
            } else {
                List<TurmaInstrutor> vinculos2 = turmaInstrutorRepository.findByTurmaIdAndIdMultiploInstrutor(id, 2);
                turmaInstrutorRepository.deleteAll(vinculos2);
            }

            // Vínculo 3 (atualiza ou remove)
            if (dto.getIdInstrutorVinculoDois() != null) {
                atualizarVinculoInstrutor(id, dto.getIdInstrutorVinculoDois(), 3, existente);
            } else {
                List<TurmaInstrutor> vinculos3 = turmaInstrutorRepository.findByTurmaIdAndIdMultiploInstrutor(id, 3);
                turmaInstrutorRepository.deleteAll(vinculos3);
            }
        } else {
            // Forçar remoção de todos vínculos 2 e 3
            List<TurmaInstrutor> vinculos2e3 = turmaInstrutorRepository
                    .findByTurmaIdAndIdMultiploInstrutorIn(id, List.of(2, 3));
            turmaInstrutorRepository.deleteAll(vinculos2e3);
        }

        // Atualizar associações com empresas
        if (dto.getIdEmpresaVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idEmpresa)
            List<Long> idsEmpresasVinculadas = turmaEmpresaRepository.findEmpresaByTurmaId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsEmpresasVinculadas.stream()
                    .filter(idEmpresa -> !dto.getIdEmpresaVinculo().contains(idEmpresa))
                    .toList();
            turmaEmpresaRepository.deleteByTurmaIdAndEmpresaIds(id, idsParaRemover);

            // Adicionar novas associações (para cada empresa que não existe na tabela)
            for (Long idEmpresa : dto.getIdEmpresaVinculo()) {
                boolean existe = turmaEmpresaRepository.existsByTurmaIdAndEmpresaId(id, idEmpresa);
                if (!existe) {
                    Empresa empresa = empresasRepository.findById(idEmpresa)
                            .orElseThrow(() -> new EntityNotFoundException("Trabalhador com ID " + idEmpresa + " não encontrada"));

                    TurmaEmpresa novaAssociacao = new TurmaEmpresa();
                    novaAssociacao.setTurma(existente);
                    novaAssociacao.setEmpresa(empresa);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    turmaEmpresaRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com curso
        if (dto.getIdCursoVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idCurso)
            List<Long> idsCursosVinculados = turmaCursoRepository.findCursoByTurmaId(id);

            // Verificar se o curso atual está na lista de associações
            Long idCursoVinculo = dto.getIdCursoVinculo();

            // Remover associações que não correspondem ao novo curso vinculado
            List<Long> idsParaRemover = idsCursosVinculados.stream()
                    .filter(idCurso -> !idCurso.equals(idCursoVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                turmaCursoRepository.deleteByTurmaIdAndCursoIds(id, idsParaRemover);
            }

            // Verificar se o curso já está associado
            boolean existe = turmaCursoRepository.existsByTurmaIdAndCursoId(id, idCursoVinculo);
            if (!existe) {
                // Recuperar o curso pelo ID
                Curso curso = cursosRepository.findById(idCursoVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Curso com ID " + idCursoVinculo + " não encontrado"));

                // Criar a nova associação
                TurmaCurso novaAssociacao = new TurmaCurso();
                novaAssociacao.setTurma(existente);
                novaAssociacao.setCurso(curso);

                // Salvar a nova associação
                turmaCursoRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com palestra
        if (dto.getIdPalestraVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idCurso)
            List<Long> idsPalestrasVinculados = turmaPalestraRepository.findPalestraByTurmaId(id);

            // Verificar se o palestra atual está na lista de associações
            Long idPalestraVinculo = dto.getIdPalestraVinculo();

            // Remover associações que não correspondem ao novo curso vinculado
            List<Long> idsParaRemover = idsPalestrasVinculados.stream()
                    .filter(idPalestra -> !idPalestra.equals(idPalestraVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                turmaPalestraRepository.deleteByTurmaIdAndPalestraIds(id, idsParaRemover);
            }

            // Verificar se a palestra já está associado
            boolean existe = turmaPalestraRepository.existsByTurmaIdAndPalestraId(id, idPalestraVinculo);
            if (!existe) {
                // Recuperar o palestra pelo ID
                Palestra palestra = palestrasRepository.findById(idPalestraVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Palestra com ID " + idPalestraVinculo + " não encontrado"));

                // Criar a nova associação
                TurmaPalestra novaAssociacao = new TurmaPalestra();
                novaAssociacao.setTurma(existente);
                novaAssociacao.setPalestra(palestra);

                // Salvar a nova associação
                turmaPalestraRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com unidade
        if (dto.getIdUnidadeVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idUnidade)
            List<Long> idsUnidadesVinculadas = turmaUnidadeRepository.findUnidadeByTurmaId(id);

            // Verificar se a unidade atual está na lista de associações
            Long idUnidadeVinculo = dto.getIdUnidadeVinculo();

            // Remover associações que não correspondem ao nova unidade vinculada
            List<Long> idsParaRemover = idsUnidadesVinculadas.stream()
                    .filter(idUnidade -> !idUnidade.equals(idUnidadeVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                turmaUnidadeRepository.deleteByTurmaIdAndUnidadeIds(id, idsParaRemover);
            }

            // Verificar se a unidade já está associado
            boolean existe = turmaUnidadeRepository.existsByTurmaIdAndUnidadeId(id, idUnidadeVinculo);
            if (!existe) {
                // Recuperar o curso pelo ID
                Unidade unidade = unidadeRepository.findById(idUnidadeVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Curso com ID " + idUnidadeVinculo + " não encontrado"));

                // Criar a nova associação
                TurmaUnidade novaAssociacao = new TurmaUnidade();
                novaAssociacao.setTurma(existente);
                novaAssociacao.setUnidade(unidade);

                // Salvar a nova associação
                turmaUnidadeRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com instrutores
        // ========== INSTRUTORES ==========
        // Vínculo 1 (sempre processar)
        if (dto.getIdInstrutorVinculo() != null) {
            atualizarVinculoInstrutor(id, dto.getIdInstrutorVinculo(), 1, existente);
        } else {
            // Remove vínculo 1 apenas se veio null
            List<TurmaInstrutor> vinculos1 = turmaInstrutorRepository.findByTurmaIdAndIdMultiploInstrutor(id, 1);
            turmaInstrutorRepository.deleteAll(vinculos1);
        }

        // Vínculos 2 e 3 (processar independente de multiplosInstrutores)
        // --- Vínculo 2 ---
        if (dto.getIdInstrutorVinculoUm() != null) {
            atualizarVinculoInstrutor(id, dto.getIdInstrutorVinculoUm(), 2, existente);
        } else {
            // Remove vínculo 2 apenas se veio null
            List<TurmaInstrutor> vinculos2 = turmaInstrutorRepository.findByTurmaIdAndIdMultiploInstrutor(id, 2);
            turmaInstrutorRepository.deleteAll(vinculos2);
        }

        // --- Vínculo 3 ---
        if (dto.getIdInstrutorVinculoDois() != null) {
            atualizarVinculoInstrutor(id, dto.getIdInstrutorVinculoDois(), 3, existente);
        } else {
            // Remove vínculo 3 apenas se veio null
            List<TurmaInstrutor> vinculos3 = turmaInstrutorRepository.findByTurmaIdAndIdMultiploInstrutor(id, 3);
            turmaInstrutorRepository.deleteAll(vinculos3);
        }


        if (!dto.getMultiplosInstrutores()) {
            // Remove apenas vínculos 2 e 3 se a flag estiver desativada
            List<TurmaInstrutor> vinculos2e3 = turmaInstrutorRepository
                    .findByTurmaIdAndIdMultiploInstrutorIn(id, List.of(2, 3));
            turmaInstrutorRepository.deleteAll(vinculos2e3);
        }

        Turma turmaAtualizado = repository.save(existente);
        return convertToDTO(turmaAtualizado);
    }

    // DELETE: Excluir turma por ID
    public void excluirTurma(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Turma com ID " + id + " não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Turma não pode ser excluída pois está vinculada a outro cadastro");
        }
    }

    // DELETE: Excluir múltiplos turmas por lista de IDs
    public void excluirTurmas(List<Long> ids) {
        List<Turma> turmas = repository.findAllById(ids);
        if (turmas.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma Turma encontrada para os IDs fornecidos");
        }
        try {
            repository.deleteAll(turmas);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Uma ou mais Turmas não podem ser excluídas pois estão vinculadas a outros cadastros.");
        }
    }

    // Método auxiliar: Converter entidade para DTO
    private TurmaDTO convertToDTO(Turma turma) {
        TurmaDTO dto = new TurmaDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dto.setId(turma.getId());
        // Aplica a condição para o campo 'ativo'
        boolean ativo = turma.getAtivo();
        LocalDate dataFimTurma = turma.getDataFim();
        if (dataFimTurma != null && dataFimTurma.isBefore(LocalDate.now())) {
            if (ativo) {
                ativo = false;
            }
        }
        dto.setAtivo(ativo);
        dto.setNome(turma.getNome());
        dto.setDataInicio(
                turma.getDataInicio() != null
                        ? turma.getDataInicio().format(formatter)
                        : null
        );
        // Calcula se data fim é passada
        boolean dataFimPassada = turma.getDataFim() != null
                && turma.getDataFim().isBefore(LocalDate.now());

        dto.setDataFimPassada(dataFimPassada);

        // Calcula mensagem se necessário
        if (!turma.getAtivo() && dataFimPassada) {
            LocalDate dataLimite = turma.getDataFim().plusDays(30);
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), dataLimite);

            String mensagem = diasRestantes > 0
                    ? "Você tem " + diasRestantes + " dias para impressão do Certificado dos alunos (até " + dataLimite.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")."
                    : "Período de impressão de Certificado expirado";

            dto.setMensagemInativacao(mensagem);
        }

        dto.setDataFim(
                turma.getDataFim() != null
                        ? turma.getDataFim().format(formatter)
                        : null
        );
        dto.setValorContratoCrm(turma.getValorContratoCrm());
        dto.setNumeroContratoCrm(turma.getNumeroContratoCrm());
        dto.setIdCidadeTreinamento(turma.getIdCidadeTreinamento());
        dto.setNomeCidadeTreinamento(turma.getNomeCidadeTreinamento());
        dto.setObservacaoNr(turma.getObservacaoNr());
        dto.setMultiplosInstrutores(turma.getMultiplosInstrutores());

        // Extrai o ID e nome do Evento vinculado (única)
        if (turma.getTurmaEventosVinculados() != null && !turma.getTurmaEventosVinculados().isEmpty()) {
            TurmaEvento turmaEvento = turma.getTurmaEventosVinculados().get(0);
            dto.setIdEventoVinculo(turmaEvento.getEvento().getId());
            dto.setNomeEventoVinculo(turmaEvento.getEvento().getNome());
        } else {
            dto.setIdEventoVinculo(null);
            dto.setNomeEventoVinculo(null);
        }

        // Extrai o ID e nome do Modalidade vinculado (única)
        if (turma.getTurmaModalidadesVinculadas() != null && !turma.getTurmaModalidadesVinculadas().isEmpty()) {
            TurmaModalidade turmaModalidade = turma.getTurmaModalidadesVinculadas().get(0);
            dto.setIdModalidadeVinculo(turmaModalidade.getModalidade().getId());
            dto.setNomeModalidadeVinculo(turmaModalidade.getModalidade().getNome());
        } else {
            dto.setIdModalidadeVinculo(null);
            dto.setNomeModalidadeVinculo(null);
        }

        // Extrai os IDs e nomes dos trabalhadores vinculados (multiplos)
        List<Long> idTrabalhadores = turma.getTurmaTrabalhadoresVinculados() != null
                ? turma.getTurmaTrabalhadoresVinculados().stream()
                .map(te -> te.getTrabalhador().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdTrabalhadorVinculo(idTrabalhadores);

        List<String> nomesTrabalhadores = turma.getTurmaTrabalhadoresVinculados().stream()
                .map(te -> te.getTrabalhador().getNome())
                .toList();
        dto.setNomeTrabalhadorVinculo(nomesTrabalhadores);

        // Extrai os IDs e nomes dos instrutores vinculados
        if (turma.getTurmaInstrutoresVinculados() != null && !turma.getTurmaInstrutoresVinculados().isEmpty()) {
            // Limpa os campos para evitar dados residuais
            dto.setIdInstrutorVinculo(null);
            dto.setNomeInstrutorVinculo(null);
            dto.setIdInstrutorVinculoUm(null);
            dto.setNomeInstrutorVinculoUm(null);
            dto.setIdInstrutorVinculoDois(null);
            dto.setNomeInstrutorVinculoDois(null);

            // Itera sobre todos os vínculos para preencher os campos corretos
            for (TurmaInstrutor turmaInstrutor : turma.getTurmaInstrutoresVinculados()) {
                switch (turmaInstrutor.getIdMultiploInstrutor()) {
                    case 1:
                        dto.setIdInstrutorVinculo(turmaInstrutor.getInstrutor().getId());
                        dto.setNomeInstrutorVinculo(turmaInstrutor.getInstrutor().getNome());
                        break;
                    case 2:
                        dto.setIdInstrutorVinculoUm(turmaInstrutor.getInstrutor().getId());
                        dto.setNomeInstrutorVinculoUm(turmaInstrutor.getInstrutor().getNome());
                        break;
                    case 3:
                        dto.setIdInstrutorVinculoDois(turmaInstrutor.getInstrutor().getId());
                        dto.setNomeInstrutorVinculoDois(turmaInstrutor.getInstrutor().getNome());
                        break;
                    default:
                        // Tratar valores inesperados, se necessário
                        break;
                }
            }
        } else {
            // Se não houver vínculos, limpa todos os campos
            dto.setIdInstrutorVinculo(null);
            dto.setNomeInstrutorVinculo(null);
            dto.setIdInstrutorVinculoUm(null);
            dto.setNomeInstrutorVinculoUm(null);
            dto.setIdInstrutorVinculoDois(null);
            dto.setNomeInstrutorVinculoDois(null);
        }

        // Extrai os IDs e nomes das empresas vinculados (multiplo)
        List<Long> idEmpresas = turma.getTurmaEmpresasVinculadas() != null
                ? turma.getTurmaEmpresasVinculadas().stream()
                .map(te -> te.getEmpresa().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdEmpresaVinculo(idEmpresas);

        List<String> nomesEmpresas = turma.getTurmaEmpresasVinculadas().stream()
                .map(te -> te.getEmpresa().getNome())
                .toList();
        dto.setNomeEmpresaVinculo(nomesEmpresas);

        // Extrai o ID e nome do Curso vinculado (unico)
        if (turma.getTurmaCursosVinculados() != null && !turma.getTurmaCursosVinculados().isEmpty()) {
            TurmaCurso turmaCurso = turma.getTurmaCursosVinculados().get(0);
            dto.setIdCursoVinculo(turmaCurso.getCurso().getId());
            dto.setNomeCursoVinculo(turmaCurso.getCurso().getNome());
        } else {
            dto.setIdCursoVinculo(null);
            dto.setNomeCursoVinculo(null);
        }

        // Extrai o ID e nome da Palestra vinculada (unico)
        if (turma.getTurmaPalestrasVinculadas() != null && !turma.getTurmaPalestrasVinculadas().isEmpty()) {
            TurmaPalestra turmaPalestra = turma.getTurmaPalestrasVinculadas().get(0);
            dto.setIdPalestraVinculo(turmaPalestra.getPalestra().getId());
            dto.setNomePalestraVinculo(turmaPalestra.getPalestra().getNomeEvento());
        } else {
            dto.setIdPalestraVinculo(null);
            dto.setNomePalestraVinculo(null);
        }

        // Extrai o ID e nome do Unidade vinculado (única)
        if (turma.getTurmaUnidadesVinculadas() != null && !turma.getTurmaUnidadesVinculadas().isEmpty()) {
            TurmaUnidade turmaUnidade = turma.getTurmaUnidadesVinculadas().get(0);
            dto.setIdUnidadeVinculo(turmaUnidade.getUnidade().getId());
            dto.setNomeUnidadeVinculo(turmaUnidade.getUnidade().getNome());
        } else {
            dto.setIdUnidadeVinculo(null);
            dto.setNomeUnidadeVinculo(null);
        }

        // Extrai os IDs e nomes dos instrutores de formação vinculados
        if (turma.getTurmaInstrutorFormacaosVinculados() != null && !turma.getTurmaInstrutorFormacaosVinculados().isEmpty()) {
            // Limpa os campos para evitar dados residuais
            dto.setIdInstrutorFormacaoVinculo(null);
            dto.setNomeInstrutorFormacaoVinculo(null);
            dto.setIdInstrutorFormacaoVinculoUm(null);
            dto.setNomeInstrutorFormacaoVinculoUm(null);
            dto.setIdInstrutorFormacaoVinculoDois(null);
            dto.setNomeInstrutorFormacaoVinculoDois(null);

            // Itera sobre todos os vínculos
            for (TurmaInstrutorFormacao turmaInstrutorFormacao : turma.getTurmaInstrutorFormacaosVinculados()) {
                switch (turmaInstrutorFormacao.getIdMultiploFormacao()) {
                    case 1:
                        dto.setIdInstrutorFormacaoVinculo(turmaInstrutorFormacao.getInstrutorFormacao().getId());
                        dto.setNomeInstrutorFormacaoVinculo(turmaInstrutorFormacao.getInstrutorFormacao().getFormacao());
                        break;
                    case 2:
                        dto.setIdInstrutorFormacaoVinculoUm(turmaInstrutorFormacao.getInstrutorFormacao().getId());
                        dto.setNomeInstrutorFormacaoVinculoUm(turmaInstrutorFormacao.getInstrutorFormacao().getFormacao());
                        break;
                    case 3:
                        dto.setIdInstrutorFormacaoVinculoDois(turmaInstrutorFormacao.getInstrutorFormacao().getId());
                        dto.setNomeInstrutorFormacaoVinculoDois(turmaInstrutorFormacao.getInstrutorFormacao().getFormacao());
                        break;
                    default:
                        // Tratar valores inesperados
                        break;
                }
            }
        } else {
            // Limpa campos se não houver vínculos
            dto.setIdInstrutorFormacaoVinculo(null);
            dto.setNomeInstrutorFormacaoVinculo(null);
            dto.setIdInstrutorFormacaoVinculoUm(null);
            dto.setNomeInstrutorFormacaoVinculoUm(null);
            dto.setIdInstrutorFormacaoVinculoDois(null);
            dto.setNomeInstrutorFormacaoVinculoDois(null);
        }

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Turma convertToEntity(TurmaDTO dto) {
        Turma turma = new Turma();
        turma.setAtivo(dto.getAtivo());
        turma.setNome(dto.getNome());

        // Converter o formato ISO 8601 para LocalDate
        turma.setDataInicio(parseDate(dto.getDataInicio()));
        turma.setDataFim(parseDate(dto.getDataFim()));
        turma.setValorContratoCrm(dto.getValorContratoCrm());
        turma.setNumeroContratoCrm(dto.getNumeroContratoCrm());
        turma.setObservacaoNr(dto.getObservacaoNr());
        turma.setIdCidadeTreinamento(dto.getIdCidadeTreinamento());
        turma.setNomeCidadeTreinamento(dto.getNomeCidadeTreinamento());
        turma.setMultiplosInstrutores(dto.getMultiplosInstrutores());

        return turma;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        String[] possibleFormats = {"yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy"};

        for (String format : possibleFormats) {
            try {
                return LocalDate.parse(dateStr.split("T")[0], DateTimeFormatter.ofPattern(format));
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new IllegalArgumentException("Formato de data inválido: " + dateStr);
    }

    private void atualizarVinculoInstrutor(Long turmaId, Long novoInstrutorId, int tipoVinculo, Turma turma) {
        // Remover todos os vínculos existentes deste tipo
        List<TurmaInstrutor> vinculosExistentes = turmaInstrutorRepository
                .findByTurmaIdAndIdMultiploInstrutor(turmaId, tipoVinculo);

        if (!vinculosExistentes.isEmpty()) {
            turmaInstrutorRepository.deleteAll(vinculosExistentes);
        }

        // Criar novo vínculo APENAS se o ID for válido
        if (novoInstrutorId != null) {
            Instrutor instrutor = instrutoresRepository.findById(novoInstrutorId)
                    .orElseThrow(() -> new EntityNotFoundException("Instrutor não encontrado: " + novoInstrutorId));

            TurmaInstrutor novoVinculo = new TurmaInstrutor();
            novoVinculo.setTurma(turma);
            novoVinculo.setInstrutor(instrutor);
            novoVinculo.setIdMultiploInstrutor(tipoVinculo);

            turmaInstrutorRepository.save(novoVinculo);
        }
    }

    private void atualizarVinculoInstrutorFormacao(Long turmaId, Long novoInstrutorFormacaoId, int tipoVinculo, Turma turma) {
        // Buscar associações existentes para este tipo de vínculo
        List<TurmaInstrutorFormacao> vinculosExistentes = turmaInstrutorFormacaoRepository
                .findByTurmaIdAndIdMultiploFormacao(turmaId, tipoVinculo); // Nome corrigido

        // Remover vínculos antigos do mesmo tipo
        if (!vinculosExistentes.isEmpty()) {
            turmaInstrutorFormacaoRepository.deleteAll(vinculosExistentes);
        }

        // Verificar se o novo instrutor já está vinculado
        boolean jaExiste = turmaInstrutorFormacaoRepository
                .existsByTurmaIdAndInstrutorFormacaoIdAndIdMultiploFormacao( // Nome corrigido
                        turmaId,
                        novoInstrutorFormacaoId,
                        tipoVinculo
                );

        if (!jaExiste) {
            InstrutorFormacao instrutorFormacao = instrutorFormacaoRepository
                    .findById(novoInstrutorFormacaoId)
                    .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada: " + novoInstrutorFormacaoId));

            TurmaInstrutorFormacao novoVinculo = new TurmaInstrutorFormacao();
            novoVinculo.setTurma(turma);
            novoVinculo.setInstrutorFormacao(instrutorFormacao);
            novoVinculo.setIdMultiploFormacao(tipoVinculo);

            turmaInstrutorFormacaoRepository.save(novoVinculo);
        }
    }
}