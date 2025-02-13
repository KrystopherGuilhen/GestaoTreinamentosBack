package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoInstrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoInstrutorRespository extends JpaRepository<PermissaoInstrutor, Long> {
}
