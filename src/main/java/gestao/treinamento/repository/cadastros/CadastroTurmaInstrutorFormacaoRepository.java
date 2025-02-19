package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.TurmaInstrutorFormacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaInstrutorFormacaoRepository extends JpaRepository<TurmaInstrutorFormacao, Long> {

    @Query("SELECT tif.instrutorFormacao.id FROM TurmaInstrutorFormacao tif WHERE tif.turma.id = :turmaId")
    List<Long> findInstrutorFormacaoByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndInstrutorFormacaoId(Long turmaId, Long instrutorFormacaoId);

    @Modifying
    @Query("DELETE FROM TurmaInstrutorFormacao tif WHERE tif.turma.id = :turmaId AND tif.instrutorFormacao.id IN :instrutorFormacaoIds")
    void deleteByTurmaIdAndInstrutorFormacaoIds(@Param("turmaId") Long turmaId, @Param("instrutorFormacaoIds") List<Long> instrutorFormacaoIds);
}
