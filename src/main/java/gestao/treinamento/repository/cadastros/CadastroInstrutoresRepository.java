package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroInstrutoresRepository extends JpaRepository<Instrutor, Long> {
}