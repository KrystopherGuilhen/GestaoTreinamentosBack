//package gestao.treinamento.repository.cadastros;
//
//import gestao.treinamento.model.entidades.MatriculaTrabalhadorTurma;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CadastroMatriculaTrabalhadorTurmaRepository extends JpaRepository<MatriculaTrabalhadorTurma, Long> {
//
//    @Query("SELECT mtt.matriculaTrabalhador.id FROM MatriculaTrabalhadorTurma mtt WHERE mtt.matriculaTrabalhador.id = :matriculaTrabalhadorId")
//    List<Long> findTurmaByMatriculaTrabalhadorId(@Param("matriculaTrabalhadorId") Long matriculaTrabalhadorId);
//
//    boolean existsByMatriculaTrabalhadorIdAndTurmaId(Long matriculaTrabalhadorId, Long turmaId);
//
//    @Modifying
//    @Query("DELETE FROM MatriculaTrabalhadorTurma mtt WHERE mtt.matriculaTrabalhador.id = :matriculaTrabalhadorId AND mtt.turma.id IN :turmasIds")
//    void deleteByMatriculaTrabalhadorIdAndTurmaIds(@Param("matriculaTrabalhadorId") Long matriculaTrabalhadorId, @Param("turmasIds") List<Long> turmasIds);
//}
