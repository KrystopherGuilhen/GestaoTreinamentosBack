//package gestao.treinamento.repository.cadastros;
//
//import gestao.treinamento.model.entidades.MatriculaTrabalhadorCurso;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CadastroMatriculaTrabalhadorCursoRepository extends JpaRepository<MatriculaTrabalhadorCurso, Long> {
//
//    @Query("SELECT mtc.curso.id FROM MatriculaTrabalhadorCurso mtc WHERE mtc.matriculaTrabalhador.id = :matriculaTrabalhadorId")
//    List<Long> findCursoByMatriculaTrabalhadorId(@Param("matriculaTrabalhadorId") Long matriculaTrabalhadorId);
//
//    boolean existsByMatriculaTrabalhadorIdAndCursoId(Long matriculaTrabalhadorId, Long cursoId);
//
//    @Modifying
//    @Query("DELETE FROM MatriculaTrabalhadorCurso mtc WHERE mtc.matriculaTrabalhador.id = :matriculaTrabalhadorId AND mtc.curso.id IN :cursosIds")
//    void deleteByMatriculaTrabalhadorIdAndCursoIds(@Param("matriculaTrabalhadorId") Long matriculaTrabalhadorId, @Param("cursosIds") List<Long> cursosIds);
//}
