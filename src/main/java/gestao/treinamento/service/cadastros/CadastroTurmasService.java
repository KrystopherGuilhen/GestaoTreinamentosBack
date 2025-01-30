package gestao.treinamento.service.cadastros;

import gestao.treinamento.model.dto.TurmaDTO;
import gestao.treinamento.model.entidade.*;
import gestao.treinamento.repository.cadastros.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

        // Verificar se há um instrutor vinculado no DTO
        if (dto.getIdInstrutorVinculo() != null) {
            // Recuperar o evento pelo ID
            Instrutor instrutor = instrutoresRepository.findById(dto.getIdInstrutorVinculo())
                    .orElseThrow(() -> new RuntimeException("Evento não encontrado: ID " + dto.getIdModalidadeVinculo()));

            // Criar a associação turma-evento
            TurmaInstrutor turmaInstrutor = new TurmaInstrutor();
            turmaInstrutor.setTurma(turma);
            turmaInstrutor.setInstrutor(instrutor);

            // Salvar a associação
            turmaInstrutorRepository.save(turmaInstrutor);
        }

        // Retornar o DTO do turma criado
        return convertToDTO(turma);
    }


    // PUT: Atualizar turma existente
    @Transactional
    public TurmaDTO atualizarTurma(Long id, TurmaDTO turmaDTO) {
        Turma turmaExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma com ID " + id + " não encontrado"));

        turmaExistente.setNome(turmaDTO.getNome());

        if (turmaDTO.getDataInicio() != null) {
            String dataInicioStr = turmaDTO.getDataInicio();
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

            turmaExistente.setDataInicio(dataInicio);
        }

        if (turmaDTO.getDataFim() != null) {
            String dataFimStr = turmaDTO.getDataFim();
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

            turmaExistente.setDataFim(dataFim);
        }

        // Atualizar associações com evento
        if (turmaDTO.getIdEventoVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idEvento)
            List<Long> idsEventosVinculadas = turmaEventoRepository.findEventoByTurmaId(id);

            // Verificar se o evento atual está na lista de associações
            Long idEventoVinculo = turmaDTO.getIdEventoVinculo();

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
                novaAssociacao.setTurma(turmaExistente);
                novaAssociacao.setEvento(evento);

                // Salvar a nova associação
                turmaEventoRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com modalidade
        if (turmaDTO.getIdModalidadeVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idModalidade)
            List<Long> idsModalidadesVinculadas = turmaModalidadeRepository.findModalidadeByTurmaId(id);

            // Verificar se o modalidade atual está na lista de associações
            Long idModalidadeVinculo = turmaDTO.getIdModalidadeVinculo();

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
                novaAssociacao.setTurma(turmaExistente);
                novaAssociacao.setModalidade(modalidade);

                // Salvar a nova associação
                turmaModalidadeRepository.save(novaAssociacao);
            }
        }

        // Atualizar associações com trabalhadores
        if (turmaDTO.getIdTrabalhadorVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idTrabalhador)
            List<Long> idsTrabalhadorsVinculadas = turmaTrabalhadorRepository.findTrabalhadorByTurmaId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsTrabalhadorsVinculadas.stream()
                    .filter(idTrabalhador -> !turmaDTO.getIdTrabalhadorVinculo().contains(idTrabalhador))
                    .toList();
            turmaTrabalhadorRepository.deleteByTurmaIdAndTrabalhadorIds(id, idsParaRemover);

            // Adicionar novas associações (para cada trabalhador que não existe na tabela)
            for (Long idTrabalhador : turmaDTO.getIdTrabalhadorVinculo()) {
                boolean existe = turmaTrabalhadorRepository.existsByTurmaIdAndTrabalhadorId(id, idTrabalhador);
                if (!existe) {
                    Trabalhador trabalhador = trabalhadoresRepository.findById(idTrabalhador)
                            .orElseThrow(() -> new EntityNotFoundException("Trabalhador com ID " + idTrabalhador + " não encontrada"));

                    TurmaTrabalhador novaAssociacao = new TurmaTrabalhador();
                    novaAssociacao.setTurma(turmaExistente);
                    novaAssociacao.setTrabalhador(trabalhador);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    turmaTrabalhadorRepository.save(novaAssociacao);
                }
            }
        }

        // Atualizar associações com instrutor
        if (turmaDTO.getIdInstrutorVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTurma e idInstrutor)
            List<Long> idsInstrutorsVinculadas = turmaInstrutorRepository.findInstrutorByTurmaId(id);

            // Verificar se o instrutor atual está na lista de associações
            Long idInstrutorVinculo = turmaDTO.getIdInstrutorVinculo();

            // Remover associações que não correspondem ao novo instrutor vinculado
            List<Long> idsParaRemover = idsInstrutorsVinculadas.stream()
                    .filter(idInstrutor -> !idInstrutor.equals(idInstrutorVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                turmaInstrutorRepository.deleteByTurmaIdAndInstrutorIds(id, idsParaRemover);
            }

            // Verificar se o instrutor já está associado
            boolean existe = turmaInstrutorRepository.existsByTurmaIdAndInstrutorId(id, idInstrutorVinculo);
            if (!existe) {
                // Recuperar o instrutor pelo ID
                Instrutor instrutor = instrutoresRepository.findById(idInstrutorVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Instrutor com ID " + idInstrutorVinculo + " não encontrado"));

                // Criar a nova associação
                TurmaInstrutor novaAssociacao = new TurmaInstrutor();
                novaAssociacao.setTurma(turmaExistente);
                novaAssociacao.setInstrutor(instrutor);

                // Salvar a nova associação
                turmaInstrutorRepository.save(novaAssociacao);
            }
        }

        Turma turmaAtualizado = repository.save(turmaExistente);
        return convertToDTO(turmaAtualizado);
    }

    // DELETE: Excluir turma por ID
    public void excluirTurma(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Turma com ID " + id + " não encontrado");
        }
        repository.deleteById(id);
    }

    // DELETE: Excluir múltiplos turmas por lista de IDs
    public void excluirTurmas(List<Long> ids) {
        List<Turma> turmas = repository.findAllById(ids);

        if (turmas.isEmpty()) {
            throw new EntityNotFoundException("Nenhum Turma encontrado para os IDs fornecidos");
        }

        repository.deleteAll(turmas);
    }

    // Método auxiliar: Converter entidade para DTO
    private TurmaDTO convertToDTO(Turma turma) {
        TurmaDTO dto = new TurmaDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dto.setId(turma.getId());
        dto.setNome(turma.getNome());
        dto.setDataInicio(
                turma.getDataInicio() != null
                        ? turma.getDataInicio().format(formatter)
                        : null
        );
        dto.setDataFim(
                turma.getDataFim() != null
                        ? turma.getDataFim().format(formatter)
                        : null
        );

        // Extrai o ID e nome do evento vinculado (único)
        if (turma.getTurmaEventosVinculados() != null && !turma.getTurmaEventosVinculados().isEmpty()) {
            TurmaEvento turmaEvento = turma.getTurmaEventosVinculados().get(0);
            dto.setIdEventoVinculo(turmaEvento.getEvento().getId());

            List<String> nomeEvento = turma.getTurmaEventosVinculados().stream()
                    .map(te -> te.getEvento().getNome())
                    .toList();
            dto.setNomeEventoVinculo(nomeEvento);
        } else {
            dto.setIdEventoVinculo(null);
            dto.setNomeEventoVinculo(null);
        }

        // Extrai o ID e nome da modalidade vinculada (única)
        if (turma.getTurmaModalidadesVinculadas() != null && !turma.getTurmaModalidadesVinculadas().isEmpty()) {
            TurmaModalidade turmaModalidade = turma.getTurmaModalidadesVinculadas().get(0);
            dto.setIdModalidadeVinculo(turmaModalidade.getModalidade().getId());

            List<String> nomeModalidades = turma.getTurmaModalidadesVinculadas().stream()
                    .map(te -> te.getModalidade().getNome())
                    .toList();
            dto.setNomeModalidadeVinculo(nomeModalidades);
        } else {
            dto.setIdModalidadeVinculo(null);
            dto.setNomeModalidadeVinculo(null);
        }

        // Extrai os IDs e nomes dos trabalhadores vinculados
        List<Long> idTrabalhadores = turma.getTurmaTrabalhadoresVinculados() != null
                ? turma.getTurmaTrabalhadoresVinculados().stream()
                .map(te -> te.getTrabalhador().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdTrabalhadorVinculo(idTrabalhadores);

        List<String> nomesTrabalhadores = turma.getTurmaTrabalhadoresVinculados().stream()
                .map(te -> te.getTrabalhador().getNome())
                .toList();

        dto.setIdTrabalhadorVinculo(idTrabalhadores);
        dto.setNomeTrabalhadorVinculo(nomesTrabalhadores);

        // Extrai o ID e nome do instrutor vinculado (único)
        if (turma.getTurmaInstrutoresVinculados() != null && !turma.getTurmaInstrutoresVinculados().isEmpty()) {
            TurmaInstrutor turmaInstrutor = turma.getTurmaInstrutoresVinculados().get(0);
            dto.setIdInstrutorVinculo(turmaInstrutor.getInstrutor().getId());

            List<String> nomeInstrutor = turma.getTurmaInstrutoresVinculados().stream()
                    .map(te -> te.getInstrutor().getNome())
                    .toList();
            dto.setNomeInstrutorVinculo(nomeInstrutor);
        } else {
            dto.setIdInstrutorVinculo(null);
            dto.setNomeInstrutorVinculo(null);
        }

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Turma convertToEntity(TurmaDTO dto) {
        Turma turma = new Turma();
        turma.setNome(dto.getNome());

        // Converter o formato ISO 8601 para LocalDate
        if (dto.getDataInicio() != null) {
            LocalDate dataNascimento = LocalDate.parse(dto.getDataInicio().split("T")[0]);
            turma.setDataInicio(dataNascimento);
        }

        // Converter o formato ISO 8601 para LocalDate
        if (dto.getDataFim() != null) {
            LocalDate dataNascimento = LocalDate.parse(dto.getDataFim().split("T")[0]);
            turma.setDataFim(dataNascimento);
        }

        return turma;
    }
}