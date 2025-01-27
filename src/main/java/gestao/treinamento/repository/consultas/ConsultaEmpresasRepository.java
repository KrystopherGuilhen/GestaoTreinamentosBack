package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.entidade.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaEmpresasRepository extends JpaRepository<Empresa, Long> {
}
