package gestao.treinamento.repository;

import gestao.treinamento.model.entidade.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroUnidadesRepository extends JpaRepository<Unidade, Long> {
}
