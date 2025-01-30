package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.entidade.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaInstrutoresRepository extends JpaRepository<Instrutor, Long> {
}
