package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Trabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroTrabalhadoresRepository extends JpaRepository<Trabalhador, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByRg(String rg);

    boolean existsByTelefone(String telefone);

    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    boolean existsByRgAndIdNot(String rg, Long id);

    boolean existsByTelefoneAndIdNot(String telefone, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
