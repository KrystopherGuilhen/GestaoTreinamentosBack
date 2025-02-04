package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.TurmaConsultaDTO;
import gestao.treinamento.model.entidades.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaTurmasRepository extends JpaRepository<Turma, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.TurmaConsultaDTO(t.id, t.nome) FROM Turma t")
    List<TurmaConsultaDTO> findAllTurmasDTO();
}
