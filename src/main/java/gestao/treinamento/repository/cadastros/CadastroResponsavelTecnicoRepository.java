package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.ResponsavelTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroResponsavelTecnicoRepository extends JpaRepository<ResponsavelTecnico, Long> {
}
