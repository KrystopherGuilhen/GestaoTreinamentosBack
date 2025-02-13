package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoEmpresaRespository extends JpaRepository<PermissaoEmpresa, Long> {
}
