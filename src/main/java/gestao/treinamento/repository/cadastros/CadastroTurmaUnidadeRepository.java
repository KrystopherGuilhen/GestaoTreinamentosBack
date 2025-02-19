package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.TurmaUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaUnidadeRepository extends JpaRepository<TurmaUnidade, Long> {

    @Query("SELECT tu.unidade.id FROM TurmaUnidade tu WHERE tu.turma.id = :turmaId")
    List<Long> findUnidadeByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndUnidadeId(Long turmaId, Long unidadeId);

    @Modifying
    @Query("DELETE FROM TurmaUnidade tu WHERE tu.turma.id = :turmaId AND tu.unidade.id IN :unidadeIds")
    void deleteByTurmaIdAndUnidadeIds(@Param("turmaId") Long turmaId, @Param("unidadeIds") List<Long> unidadeIds);
}
