package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Industria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroIndustriasRepository extends JpaRepository<Industria, Long> {
}
