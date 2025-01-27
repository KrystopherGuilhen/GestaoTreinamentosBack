//package gestao.treinamento.service.cadastros;
//
//import gestao.treinamento.model.dto.TurmaDTO;
//import gestao.treinamento.model.entidade.Empresa;
//import gestao.treinamento.model.entidade.Turma;
//import gestao.treinamento.model.entidade.TurmaEmpresa;
//import gestao.treinamento.repository.cadastros.CadastroEmpresasRepository;
//import gestao.treinamento.repository.cadastros.CadastroTurmaEmpresaRepository;
//import gestao.treinamento.repository.cadastros.CadastroTurmasRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//public class CadastroTurmasService {
//
//    @Autowired
//    private final CadastroTurmasRepository repository;
//    private final CadastroEmpresasRepository empresaRepository;
//    private final CadastroTurmaEmpresaRepository turmaEmpresaRepository;
//
//    // GET: Buscar todos os turmas
//    public List<TurmaDTO> consultaCadastro() {
//        List<Turma> turmas = repository.findAll();
//
//        return turmas.stream().map(this::convertToDTO).toList();
//    }
//
//    // POST: Criar novo turma
//    @Transactional
//    public TurmaDTO criarTurma(TurmaDTO dto) {
//        // Converter o DTO para entidade turma
//        Turma turma = convertToEntity(dto);
//
//        // Salvar o turma e obter o ID gerado
//        turma = repository.save(turma);
//
//        // Verificar se há empresas vinculadas no DTO
//        if (dto.getIdEmpresaVinculo() != null && !dto.getIdEmpresaVinculo().isEmpty()) {
//            for (Long idEmpresa : dto.getIdEmpresaVinculo()) {
//                // Recuperar a empresa pelo ID (se necessário)
//                Empresa empresa = empresaRepository.findById(idEmpresa)
//                        .orElseThrow(() -> new RuntimeException("Empresa não encontrada: ID " + idEmpresa));
//
//                // Criar a associação turma-empresa
//                TurmaEmpresa turmaEmpresas = new TurmaEmpresa();
//                turmaEmpresas.setTurma(turma);
//                turmaEmpresas.setEmpresa(empresa);
//
//                // Salvar a associação
//                turmaEmpresaRepository.save(turmaEmpresas);
//            }
//        }
//
//        // Retornar o DTO do turma criado
//        return convertToDTO(turma);
//    }
//
//
//    // PUT: Atualizar turma existente
//    @Transactional
//    public TurmaDTO atualizarTurma(Long id, TurmaDTO turmaDTO) {
//        Turma turmaExistente = repository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Turma com ID " + id + " não encontrado"));
//
//        turmaExistente.setNomeEvento(turmaDTO.getNomeEvento());
//        turmaExistente.setCidadeEvento(turmaDTO.getCidadeEvento());
//
//        if (turmaDTO.getDataInicio() != null) {
//            String dataInicioStr = turmaDTO.getDataInicio();
//            LocalDate dataInicio = null;
//
//            try {
//                // Primeiro, tenta converter do formato ISO 8601
//                if (dataInicioStr.contains("T")) {
//                    dataInicio = LocalDate.parse(dataInicioStr.split("T")[0]);
//                } else {
//                    // Tenta converter do formato dd/MM/yyyy
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                    dataInicio = LocalDate.parse(dataInicioStr, formatter);
//                }
//            } catch (DateTimeParseException e) {
//                throw new IllegalArgumentException("Formato de data inválido: " + dataInicioStr);
//            }
//
//            turmaExistente.setDataInicio(dataInicio);
//        }
//
//        if (turmaDTO.getDataFim() != null) {
//            String dataFimStr = turmaDTO.getDataFim();
//            LocalDate dataFim = null;
//
//            try {
//                // Primeiro, tenta converter do formato ISO 8601
//                if (dataFimStr.contains("T")) {
//                    dataFim = LocalDate.parse(dataFimStr.split("T")[0]);
//                } else {
//                    // Tenta converter do formato dd/MM/yyyy
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                    dataFim = LocalDate.parse(dataFimStr, formatter);
//                }
//            } catch (DateTimeParseException e) {
//                throw new IllegalArgumentException("Formato de data inválido: " + dataFimStr);
//            }
//
//            turmaExistente.setDataFim(dataFim);
//        }
//
//        turmaExistente.setValorContratoCrm(turmaDTO.getValorContratoCrm());
//        turmaExistente.setNumeroContratoCrm(turmaDTO.getNumeroContratoCrm());
//
//        // Atualizar associações com empresas
//        if (turmaDTO.getIdEmpresaVinculo() != null) {
//            // Recuperar as associações existentes (com a chave composta idTurma e idEmpresa)
//            List<Long> idsEmpresasVinculadas = turmaEmpresaRepository.findEmpresasByTurmaId(id);
//
//            // Remover associações que não estão mais na lista
//            List<Long> idsParaRemover = idsEmpresasVinculadas.stream()
//                    .filter(idEmpresa -> !turmaDTO.getIdEmpresaVinculo().contains(idEmpresa))
//                    .toList();
//            turmaEmpresaRepository.deleteByTurmaIdAndEmpresaIds(id, idsParaRemover);
//
//            // Adicionar novas associações (para cada empresa que não existe na tabela)
//            for (Long idEmpresa : turmaDTO.getIdEmpresaVinculo()) {
//                boolean existe = turmaEmpresaRepository.existsByTurmaIdAndEmpresaId(id, idEmpresa);
//                if (!existe) {
//                    Empresa empresa = empresaRepository.findById(idEmpresa)
//                            .orElseThrow(() -> new EntityNotFoundException("Empresa com ID " + idEmpresa + " não encontrada"));
//
//                    TurmaEmpresa novaAssociacao = new TurmaEmpresa();
//                    novaAssociacao.setTurma(turmaExistente);
//                    novaAssociacao.setEmpresa(empresa);
//
//                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
//                    turmaEmpresaRepository.save(novaAssociacao);
//                }
//            }
//        }
//
//        Turma turmaAtualizado = repository.save(turmaExistente);
//        return convertToDTO(turmaAtualizado);
//    }
//
//    // DELETE: Excluir turma por ID
//    public void excluirTurma(Long id) {
//        if (!repository.existsById(id)) {
//            throw new EntityNotFoundException("Turma com ID " + id + " não encontrado");
//        }
//        repository.deleteById(id);
//    }
//
//    // DELETE: Excluir múltiplos turmas por lista de IDs
//    public void excluirTurmas(List<Long> ids) {
//        List<Turma> turmas = repository.findAllById(ids);
//
//        if (turmas.isEmpty()) {
//            throw new EntityNotFoundException("Nenhum Turma encontrado para os IDs fornecidos");
//        }
//
//        repository.deleteAll(turmas);
//    }
//
//    // Método auxiliar: Converter entidade para DTO
//    private TurmaDTO convertToDTO(Turma turma) {
//        TurmaDTO dto = new TurmaDTO();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        dto.setId(turma.getId());
//        dto.setNomeEvento(turma.getNomeEvento());
//        dto.setCidadeEvento(turma.getCidadeEvento());
//        dto.setDataInicio(
//                turma.getDataInicio() != null
//                        ? turma.getDataInicio().format(formatter)
//                        : null
//        );
//        dto.setDataFim(
//                turma.getDataFim() != null
//                        ? turma.getDataFim().format(formatter)
//                        : null
//        );
//        dto.setValorContratoCrm(turma.getValorContratoCrm());
//        dto.setValorContratoCrm(turma.getValorContratoCrm());
//        dto.setNumeroContratoCrm(turma.getNumeroContratoCrm());
//
//        // Extrai os IDs e nomes das empresas vinculadas
//        List<Long> idEmpresas = turma.getEmpresasVinculadas() != null
//                ? turma.getEmpresasVinculadas().stream()
//                .map(te -> te.getEmpresa().getId())
//                .toList()
//                : new ArrayList<>();
//        dto.setIdEmpresaVinculo(idEmpresas);
//
//        List<String> nomesEmpresas = turma.getEmpresasVinculadas().stream()
//                .map(te -> te.getEmpresa().getNome())
//                .toList();
//
//        dto.setIdEmpresaVinculo(idEmpresas);
//        dto.setNomeEmpresaVinculo(nomesEmpresas);
//
//        return dto;
//    }
//
//    // Método auxiliar: Converter DTO para entidade
//    private Turma convertToEntity(TurmaDTO dto) {
//        Turma turma = new Turma();
//        turma.setNomeEvento(dto.getNomeEvento());
//        turma.setCidadeEvento(dto.getCidadeEvento());
//
//        // Converter o formato ISO 8601 para LocalDate
//        if (dto.getDataInicio() != null) {
//            LocalDate dataNascimento = LocalDate.parse(dto.getDataInicio().split("T")[0]);
//            turma.setDataInicio(dataNascimento);
//        }
//
//        // Converter o formato ISO 8601 para LocalDate
//        if (dto.getDataFim() != null) {
//            LocalDate dataNascimento = LocalDate.parse(dto.getDataFim().split("T")[0]);
//            turma.setDataFim(dataNascimento);
//        }
//
//        turma.setValorContratoCrm(dto.getValorContratoCrm());
//        turma.setNumeroContratoCrm(dto.getNumeroContratoCrm());
//
//        return turma;
//    }
//}
//}
