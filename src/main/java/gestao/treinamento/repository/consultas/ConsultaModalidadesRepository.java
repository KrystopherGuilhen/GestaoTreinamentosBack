package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.entidade.Modalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaModalidadesRepository extends JpaRepository<Modalidade, Long> {
}
