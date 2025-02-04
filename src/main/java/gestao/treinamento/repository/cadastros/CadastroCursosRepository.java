package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroCursosRepository extends JpaRepository<Curso, Long> {
}
