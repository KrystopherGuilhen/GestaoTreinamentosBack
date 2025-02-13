package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoPalestra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoPalestraRespository extends JpaRepository<PermissaoPalestra, Long> {
}
