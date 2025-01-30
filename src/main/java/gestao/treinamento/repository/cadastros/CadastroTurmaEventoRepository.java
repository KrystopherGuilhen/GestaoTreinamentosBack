package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidade.TurmaEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaEventoRepository extends JpaRepository<TurmaEvento, Long> {

    @Query("SELECT te.evento.id FROM TurmaEvento te WHERE te.turma.id = :turmaId")
    List<Long> findEventoByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndEventoId(Long turmaId, Long eventoId);

    @Modifying
    @Query("DELETE FROM TurmaEvento te WHERE te.turma.id = :turmaId AND te.evento.id IN :eventoIds")
    void deleteByTurmaIdAndEventoIds(@Param("turmaId") Long turmaId, @Param("eventoIds") List<Long> eventoIds);
}
