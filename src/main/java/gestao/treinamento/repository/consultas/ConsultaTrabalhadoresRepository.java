package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.entidade.Trabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaTrabalhadoresRepository extends JpaRepository<Trabalhador, Long> {
}
