package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PermissaoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroPermissaoCursoRespository extends JpaRepository<PermissaoCurso, Long> {
}
