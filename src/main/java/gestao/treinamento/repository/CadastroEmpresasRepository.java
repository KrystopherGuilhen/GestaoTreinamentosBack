package gestao.treinamento.repository;

import gestao.treinamento.model.entidade.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroEmpresasRepository extends JpaRepository<Empresa, Long> {
}
