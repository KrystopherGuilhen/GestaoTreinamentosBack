package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoTrabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoTrabalhadorRespository extends JpaRepository<PermissaoTrabalhador, Long> {
}
