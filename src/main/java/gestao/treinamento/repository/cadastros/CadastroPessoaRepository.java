package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPessoaRepository extends JpaRepository<Pessoa, Long> {
}
