package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.TurmaPalestra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaPalestraRepository extends JpaRepository<TurmaPalestra, Long> {

    @Query("SELECT tc.palestra.id FROM TurmaPalestra tc WHERE tc.turma.id = :turmaId")
    List<Long> findPalestraByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndPalestraId(Long turmaId, Long palestraId);

    @Modifying
    @Query("DELETE FROM TurmaPalestra tc WHERE tc.turma.id = :turmaId AND tc.palestra.id IN :palestraIds")
    void deleteByTurmaIdAndPalestraIds(@Param("turmaId") Long turmaId, @Param("palestraIds") List<Long> palestraIds);
}