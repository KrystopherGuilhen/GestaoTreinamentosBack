package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidade.TurmaTrabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaTrabalhadorRepository extends JpaRepository<TurmaTrabalhador, Long> {

    @Query("SELECT tt.trabalhador.id FROM TurmaTrabalhador tt WHERE tt.turma.id = :turmaId")
    List<Long> findTrabalhadorByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndTrabalhadorId(Long turmaId, Long trabalhadorId);

    @Modifying
    @Query("DELETE FROM TurmaTrabalhador tt WHERE tt.turma.id = :turmaId AND tt.trabalhador.id IN :trabalhadorIds")
    void deleteByTurmaIdAndTrabalhadorIds(@Param("turmaId") Long turmaId, @Param("trabalhadorIds") List<Long> trabalhadorIds);
}
