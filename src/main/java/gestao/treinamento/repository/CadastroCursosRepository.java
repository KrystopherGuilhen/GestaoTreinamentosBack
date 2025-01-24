package gestao.treinamento.repository;

import gestao.treinamento.model.entidade.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroCursosRepository extends JpaRepository<Curso, Long> {
}
