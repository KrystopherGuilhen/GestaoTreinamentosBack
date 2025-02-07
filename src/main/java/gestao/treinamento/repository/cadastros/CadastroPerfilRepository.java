package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPerfilRepository extends JpaRepository<Perfil, Long> {
}
