package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.NivelVisibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroNivelVisibilidadeRepository extends JpaRepository<NivelVisibilidade, Long> {
}
