package gestao.treinamento.repository;

import gestao.treinamento.model.entidade.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroEmpresaRepository extends JpaRepository<Empresa, Long> {
}
