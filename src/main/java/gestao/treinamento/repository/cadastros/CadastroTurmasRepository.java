package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroTurmasRepository extends JpaRepository<Turma, Long> {
}
