package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroEmpresasRepository extends JpaRepository<Empresa, Long> {
}
