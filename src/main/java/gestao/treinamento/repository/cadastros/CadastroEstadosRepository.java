package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Estados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadastroEstadosRepository extends JpaRepository<Estados, Long> {

    Optional<Estados> findBySigla(String sigla);
}
