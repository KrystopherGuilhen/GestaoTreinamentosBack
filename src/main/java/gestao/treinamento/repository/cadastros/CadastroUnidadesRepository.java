package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroUnidadesRepository extends JpaRepository<Unidade, Long> {
}
