package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.entidades.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultaTurmasRepository extends JpaRepository<Turma, Long> {
}