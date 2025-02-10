package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.NivelPermissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroNivelPermissaoRepository extends JpaRepository<NivelPermissao, Long> {
}
