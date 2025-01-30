package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidade.TurmaModalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaModalidadeRepository extends JpaRepository<TurmaModalidade, Long> {

    @Query("SELECT tm.modalidade.id FROM TurmaModalidade tm WHERE tm.turma.id = :turmaId")
    List<Long> findModalidadeByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndModalidadeId(Long turmaId, Long modalidadeId);

    @Modifying
    @Query("DELETE FROM TurmaModalidade tm WHERE tm.turma.id = :turmaId AND tm.modalidade.id IN :modalidadeIds")
    void deleteByTurmaIdAndModalidadeIds(@Param("turmaId") Long turmaId, @Param("modalidadeIds") List<Long> modalidadeIds);
}
