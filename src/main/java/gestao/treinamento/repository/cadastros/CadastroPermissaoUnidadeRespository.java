package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoUnidadeRespository extends JpaRepository<PermissaoUnidade, Long> {
}
