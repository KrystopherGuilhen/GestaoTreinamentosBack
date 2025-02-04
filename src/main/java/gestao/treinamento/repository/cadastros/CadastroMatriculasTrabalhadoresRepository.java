package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.MatriculaTrabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroMatriculasTrabalhadoresRepository extends JpaRepository<MatriculaTrabalhador, Long> {
}
