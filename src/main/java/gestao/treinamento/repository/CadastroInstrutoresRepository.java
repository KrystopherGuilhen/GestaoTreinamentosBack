package gestao.treinamento.repository;

import gestao.treinamento.model.entidade.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroInstrutoresRepository extends JpaRepository<Instrutor, Long> {


}
