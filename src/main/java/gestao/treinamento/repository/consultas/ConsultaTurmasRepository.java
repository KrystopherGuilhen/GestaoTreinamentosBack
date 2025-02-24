package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.entidades.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultaTurmasRepository extends JpaRepository<Turma, Long> {

    @Query("SELECT t FROM Turma t " +
            "LEFT JOIN FETCH t.turmaTrabalhadoresVinculados tt " +
            "LEFT JOIN FETCH tt.trabalhador " +
            "LEFT JOIN FETCH t.turmaCursosVinculados tc " +
            "LEFT JOIN FETCH tc.curso " +
            "LEFT JOIN FETCH t.turmaModalidadesVinculadas tm " +
            "LEFT JOIN FETCH tm.modalidade " +
            "LEFT JOIN FETCH t.turmaUnidadesVinculadas tu " +
            "LEFT JOIN FETCH tu.unidade " +
            "WHERE t.id = :idTurma")
    Optional<Turma> findByIdWithRelations(@Param("idTurma") Long idTurma);
}