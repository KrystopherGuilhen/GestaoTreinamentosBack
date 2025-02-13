package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoPerfilRespository extends JpaRepository<PermissaoPerfil, Long> {
}
