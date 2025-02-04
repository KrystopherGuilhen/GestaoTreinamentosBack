package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.TurmaCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaCursoRepository extends JpaRepository<TurmaCurso, Long> {

    @Query("SELECT tc.curso.id FROM TurmaCurso tc WHERE tc.turma.id = :turmaId")
    List<Long> findCursoByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndCursoId(Long turmaId, Long cursoId);

    @Modifying
    @Query("DELETE FROM TurmaCurso tc WHERE tc.turma.id = :turmaId AND tc.curso.id IN :cursoIds")
    void deleteByTurmaIdAndCursoIds(@Param("turmaId") Long turmaId, @Param("cursoIds") List<Long> cursoIds);
}
