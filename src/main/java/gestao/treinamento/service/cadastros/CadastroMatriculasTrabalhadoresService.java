//package gestao.treinamento.service.cadastros;
//
//import gestao.treinamento.model.dto.cadastros.MatriculaTrabalhadorDTO;
//import gestao.treinamento.model.entidades.*;
//import gestao.treinamento.repository.cadastros.*;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//public class CadastroMatriculasTrabalhadoresService {
//
//    @Autowired
//    private final CadastroMatriculasTrabalhadoresRepository repository;
//
//    private final CadastroTrabalhadoresRepository trabalhadoresRepository;
//    private final CadastroMatriculaTrabalhadorVinculoRepository matriculaTrabalhadorVinculoRepository;
//
//    private final CadastroTurmasRepository turmasRepository;
//    private final CadastroMatriculaTrabalhadorTurmaRepository matriculaTrabalhadorTurmaRepository;
//
//    private final CadastroCursosRepository cursosRepository;
//    private final CadastroMatriculaTrabalhadorCursoRepository matriculaTrabalhadorCursoRepository;
//
//    // GET: Buscar todos os matriculaTrabalhadors
//    public List<MatriculaTrabalhadorDTO> consultaCadastro() {
//        List<MatriculaTrabalhador> matriculaTrabalhadors = repository.findAll();
//
//        return matriculaTrabalhadors.stream().map(this::convertToDTO).toList();
//    }
//
//    // POST: Criar novo matriculaTrabalhador
//    @Transactional
//    public MatriculaTrabalhadorDTO criarMatriculaTrabalhador(MatriculaTrabalhadorDTO dto) {
//        // Converter o DTO para entidade matriculaTrabalhador
//        MatriculaTrabalhador matriculaTrabalhador = convertToEntity(dto);
//
//        // Salvar o matriculaTrabalhador e obter o ID gerado
//        matriculaTrabalhador = repository.save(matriculaTrabalhador);
//
//        // Verificar se há um trabalhador vinculado no DTO
//        if (dto.getIdTrabalhadorVinculo() != null) {
//            // Recuperar o evento pelo ID
//            Trabalhador trabalhador = trabalhadoresRepository.findById(dto.getIdTrabalhadorVinculo())
//                    .orElseThrow(() -> new RuntimeException("Evento não encontrado: ID " + dto.getIdTrabalhadorVinculo()));
//
//            // Criar a associação matriculaTrabalhador-evento
//            MatriculaTrabalhadorVinculo matriculaTrabalhadorVinculo = new MatriculaTrabalhadorVinculo();
//            matriculaTrabalhadorVinculo.setMatriculaTrabalhador(matriculaTrabalhador);
//            matriculaTrabalhadorVinculo.setTrabalhador(trabalhador);
//
//            // Salvar a associação
//            matriculaTrabalhadorVinculoRepository.save(matriculaTrabalhadorVinculo);
//        }
//
//        // Verificar se há um turma vinculado no DTO
//        if (dto.getIdTurmaVinculo() != null) {
//            // Recuperar o evento pelo ID
//            Turma turma = turmasRepository.findById(dto.getIdTurmaVinculo())
//                    .orElseThrow(() -> new RuntimeException("Evento não encontrado: ID " + dto.getIdTurmaVinculo()));
//
//            // Criar a associação matriculaTrabalhador-evento
//            MatriculaTrabalhadorTurma matriculaTrabalhadorTurma = new MatriculaTrabalhadorTurma();
//            matriculaTrabalhadorTurma.setMatriculaTrabalhador(matriculaTrabalhador);
//            matriculaTrabalhadorTurma.setTurma(turma);
//
//            // Salvar a associação
//            matriculaTrabalhadorTurmaRepository.save(matriculaTrabalhadorTurma);
//        }
//
//        // Verificar se há um instrutor vinculado no DTO
//        if (dto.getIdCursoVinculo() != null) {
//            // Recuperar o evento pelo ID
//            Curso curso = cursosRepository.findById(dto.getIdCursoVinculo())
//                    .orElseThrow(() -> new RuntimeException("Evento não encontrado: ID " + dto.getIdCursoVinculo()));
//
//            // Criar a associação matriculaTrabalhador-evento
//            MatriculaTrabalhadorCurso matriculaTrabalhadorCurso = new MatriculaTrabalhadorCurso();
//            matriculaTrabalhadorCurso.setMatriculaTrabalhador(matriculaTrabalhador);
//            matriculaTrabalhadorCurso.setCurso(curso);
//
//            // Salvar a associação
//            matriculaTrabalhadorCursoRepository.save(matriculaTrabalhadorCurso);
//        }
//
//        // Retornar o DTO do matriculaTrabalhador criado
//        return convertToDTO(matriculaTrabalhador);
//    }
//
//
//    // PUT: Atualizar matriculaTrabalhador existente
//    @Transactional
//    public MatriculaTrabalhadorDTO atualizarMatriculaTrabalhador(Long id, MatriculaTrabalhadorDTO matriculaTrabalhadorDTO) {
//        MatriculaTrabalhador matriculaTrabalhadorExistente = repository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("MatriculaTrabalhador com ID " + id + " não encontrado"));
//
//        matriculaTrabalhadorExistente.setCargaHorariaTotal(matriculaTrabalhadorDTO.getCargaHorariaTotal());
//
//        // Atualizar associações com trabalhador
//        if (matriculaTrabalhadorDTO.getIdTrabalhadorVinculo() != null) {
//            // Recuperar as associações existentes (com a chave composta idMatriculaTrabalhador e idTrabalhador)
//            List<Long> idsEventosVinculadas = matriculaTrabalhadorVinculoRepository.findTrabalhadorByMatriculaTrabalhadorId(id);
//
//            // Verificar se o trabalhador atual está na lista de associações
//            Long idTrabalhadorVinculo = matriculaTrabalhadorDTO.getIdTrabalhadorVinculo();
//
//            // Remover associações que não correspondem ao novo evento vinculado
//            List<Long> idsParaRemover = idsEventosVinculadas.stream()
//                    .filter(idEvento -> !idEvento.equals(idTrabalhadorVinculo))
//                    .toList();
//            if (!idsParaRemover.isEmpty()) {
//                matriculaTrabalhadorVinculoRepository.deleteByMatriculaTrabalhadorIdAndTrabalhadoresIds(id, idsParaRemover);
//            }
//
//            // Verificar se o trabalhador já está associado
//            boolean existe = matriculaTrabalhadorVinculoRepository.existsByMatriculaTrabalhadorIdAndTrabalhadorId(id, idTrabalhadorVinculo);
//            if (!existe) {
//                // Recuperar o evento pelo ID
//                Trabalhador trabalhador = trabalhadoresRepository.findById(idTrabalhadorVinculo)
//                        .orElseThrow(() -> new EntityNotFoundException("Evento com ID " + idTrabalhadorVinculo + " não encontrado"));
//
//                // Criar a nova associação
//                MatriculaTrabalhadorVinculo novaAssociacao = new MatriculaTrabalhadorVinculo();
//                novaAssociacao.setMatriculaTrabalhador(matriculaTrabalhadorExistente);
//                novaAssociacao.setTrabalhador(trabalhador);
//
//                // Salvar a nova associação
//                matriculaTrabalhadorVinculoRepository.save(novaAssociacao);
//            }
//        }
//
//        // Atualizar associações com turma
//        if (matriculaTrabalhadorDTO.getIdTurmaVinculo() != null) {
//            // Recuperar as associações existentes (com a chave composta idMatriculaTrabalhador e idTurma)
//            List<Long> idsTurmasVinculadas = matriculaTrabalhadorTurmaRepository.findTurmaByMatriculaTrabalhadorId(id);
//
//            // Verificar se o turma atual está na lista de associações
//            Long idTurmaVinculo = matriculaTrabalhadorDTO.getIdTurmaVinculo();
//
//            // Remover associações que não correspondem ao novo turma vinculado
//            List<Long> idsParaRemover = idsTurmasVinculadas.stream()
//                    .filter(idTurma -> !idTurma.equals(idTurmaVinculo))
//                    .toList();
//            if (!idsParaRemover.isEmpty()) {
//                matriculaTrabalhadorTurmaRepository.deleteByMatriculaTrabalhadorIdAndTurmaIds(id, idsParaRemover);
//            }
//
//            // Verificar se o turma já está associado
//            boolean existe = matriculaTrabalhadorTurmaRepository.existsByMatriculaTrabalhadorIdAndTurmaId(id, idTurmaVinculo);
//            if (!existe) {
//                // Recuperar o turma pelo ID
//                Turma turma = turmasRepository.findById(idTurmaVinculo)
//                        .orElseThrow(() -> new EntityNotFoundException("Turma com ID " + idTurmaVinculo + " não encontrado"));
//
//                // Criar a nova associação
//                MatriculaTrabalhadorTurma novaAssociacao = new MatriculaTrabalhadorTurma();
//                novaAssociacao.setMatriculaTrabalhador(matriculaTrabalhadorExistente);
//                novaAssociacao.setTurma(turma);
//
//                // Salvar a nova associação
//                matriculaTrabalhadorTurmaRepository.save(novaAssociacao);
//            }
//        }
//
//        // Atualizar associações com curso
//        if (matriculaTrabalhadorDTO.getIdCursoVinculo() != null) {
//            // Recuperar as associações existentes (com a chave composta idMatriculaTrabalhador e idCurso)
//            List<Long> idsCursosVinculadas = matriculaTrabalhadorCursoRepository.findCursoByMatriculaTrabalhadorId(id);
//
//            // Verificar se o curso atual está na lista de associações
//            Long idCursoVinculo = matriculaTrabalhadorDTO.getIdCursoVinculo();
//
//            // Remover associações que não correspondem ao novo curso vinculado
//            List<Long> idsParaRemover = idsCursosVinculadas.stream()
//                    .filter(idCurso -> !idCurso.equals(idCursoVinculo))
//                    .toList();
//            if (!idsParaRemover.isEmpty()) {
//                matriculaTrabalhadorCursoRepository.deleteByMatriculaTrabalhadorIdAndCursoIds(id, idsParaRemover);
//            }
//
//            // Verificar se o curso já está associado
//            boolean existe = matriculaTrabalhadorCursoRepository.existsByMatriculaTrabalhadorIdAndCursoId(id, idCursoVinculo);
//            if (!existe) {
//                // Recuperar o curso pelo ID
//                Curso curso = cursosRepository.findById(idCursoVinculo)
//                        .orElseThrow(() -> new EntityNotFoundException("Curso com ID " + idCursoVinculo + " não encontrado"));
//
//                // Criar a nova associação
//                MatriculaTrabalhadorCurso novaAssociacao = new MatriculaTrabalhadorCurso();
//                novaAssociacao.setMatriculaTrabalhador(matriculaTrabalhadorExistente);
//                novaAssociacao.setCurso(curso);
//
//                // Salvar a nova associação
//                matriculaTrabalhadorCursoRepository.save(novaAssociacao);
//            }
//        }
//
//        MatriculaTrabalhador matriculaTrabalhadorAtualizado = repository.save(matriculaTrabalhadorExistente);
//        return convertToDTO(matriculaTrabalhadorAtualizado);
//    }
//
//    // DELETE: Excluir matriculaTrabalhador por ID
//    public void excluirMatriculaTrabalhador(Long id) {
//        if (!repository.existsById(id)) {
//            throw new EntityNotFoundException("MatriculaTrabalhador com ID " + id + " não encontrado");
//        }
//        repository.deleteById(id);
//    }
//
//    // DELETE: Excluir múltiplos matriculaTrabalhadors por lista de IDs
//    public void excluirMatriculasTrabalhadores(List<Long> ids) {
//        List<MatriculaTrabalhador> matriculaTrabalhadors = repository.findAllById(ids);
//
//        if (matriculaTrabalhadors.isEmpty()) {
//            throw new EntityNotFoundException("Nenhum MatriculaTrabalhador encontrado para os IDs fornecidos");
//        }
//
//        repository.deleteAll(matriculaTrabalhadors);
//    }
//
//    // Método auxiliar: Converter entidade para DTO
//    private MatriculaTrabalhadorDTO convertToDTO(MatriculaTrabalhador matriculaTrabalhador) {
//        MatriculaTrabalhadorDTO dto = new MatriculaTrabalhadorDTO();
//
//        dto.setId(matriculaTrabalhador.getId());
//
//        // Extrai o ID e nome do trabalhador vinculado (único)
//        if (matriculaTrabalhador.getMatriculaTrabalhadoresVinculados() != null && !matriculaTrabalhador.getMatriculaTrabalhadoresVinculados().isEmpty()) {
//            MatriculaTrabalhadorVinculo matriculaTrabalhadorVinculo = matriculaTrabalhador.getMatriculaTrabalhadoresVinculados().get(0);
//            dto.setIdTrabalhadorVinculo(matriculaTrabalhadorVinculo.getTrabalhador().getId());
//
//            List<String> nomeTrabalahdorVinculo = matriculaTrabalhador.getMatriculaTrabalhadoresVinculados().stream()
//                    .map(te -> te.getTrabalhador().getNome())
//                    .toList();
//            dto.setNomeTrabalhadorVinculo(nomeTrabalahdorVinculo);
//        } else {
//            dto.setIdTrabalhadorVinculo(null);
//            dto.setNomeTrabalhadorVinculo(null);
//        }
//
//        // Extrai o ID e nome da turma vinculada (única)
//        if (matriculaTrabalhador.getMatriculaTrabalhadorTurmasVinculadas() != null && !matriculaTrabalhador.getMatriculaTrabalhadorTurmasVinculadas().isEmpty()) {
//            MatriculaTrabalhadorTurma matriculaTrabalhadorTurma = matriculaTrabalhador.getMatriculaTrabalhadorTurmasVinculadas().get(0);
//            dto.setIdTurmaVinculo(matriculaTrabalhadorTurma.getTurma().getId());
//
//            List<String> nomeTurmas = matriculaTrabalhador.getMatriculaTrabalhadorTurmasVinculadas().stream()
//                    .map(te -> te.getTurma().getNome())
//                    .toList();
//            dto.setNomeTurmaVinculo(nomeTurmas);
//        } else {
//            dto.setIdTurmaVinculo(null);
//            dto.setNomeTurmaVinculo(null);
//        }
//
//        // Extrai o ID e nome do curso vinculado (único)
//        if (matriculaTrabalhador.getMatriculaTrabalhadorCursosVinculados() != null && !matriculaTrabalhador.getMatriculaTrabalhadorCursosVinculados().isEmpty()) {
//            MatriculaTrabalhadorCurso matriculaTrabalhadorCurso = matriculaTrabalhador.getMatriculaTrabalhadorCursosVinculados().get(0);
//            dto.setIdCursoVinculo(matriculaTrabalhadorCurso.getCurso().getId());
//
//            List<String> nomeCurso = matriculaTrabalhador.getMatriculaTrabalhadorCursosVinculados().stream()
//                    .map(te -> te.getCurso().getNome())
//                    .toList();
//            dto.setNomeCursoVinculo(nomeCurso);
//        } else {
//            dto.setIdCursoVinculo(null);
//            dto.setNomeCursoVinculo(null);
//        }
//
//        dto.setCargaHorariaTotal(matriculaTrabalhador.getCargaHorariaTotal());
//
//        return dto;
//    }
//
//    // Método auxiliar: Converter DTO para entidade
//    private MatriculaTrabalhador convertToEntity(MatriculaTrabalhadorDTO dto) {
//        MatriculaTrabalhador matriculaTrabalhador = new MatriculaTrabalhador();
//        matriculaTrabalhador.setCargaHorariaTotal(dto.getCargaHorariaTotal());
//
//        return matriculaTrabalhador;
//    }
//}
