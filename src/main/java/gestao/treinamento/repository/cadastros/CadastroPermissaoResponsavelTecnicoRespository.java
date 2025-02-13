package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoResponsavelTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoResponsavelTecnicoRespository extends JpaRepository<PermissaoResponsavelTecnico, Long> {
}
