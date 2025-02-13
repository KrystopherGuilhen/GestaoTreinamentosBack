package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoTurma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoTurmaRespository extends JpaRepository<PermissaoTurma, Long> {
}
