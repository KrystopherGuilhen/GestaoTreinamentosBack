package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Modalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroModalidadesRepository extends JpaRepository<Modalidade, Long> {
}
