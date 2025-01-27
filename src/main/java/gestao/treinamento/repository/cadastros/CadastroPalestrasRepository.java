package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidade.Palestra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPalestrasRepository extends JpaRepository<Palestra, Long> {
}
