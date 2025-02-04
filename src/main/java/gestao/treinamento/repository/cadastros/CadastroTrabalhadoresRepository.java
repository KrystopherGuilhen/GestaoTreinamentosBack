package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Trabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroTrabalhadoresRepository extends JpaRepository<Trabalhador, Long> {
}
