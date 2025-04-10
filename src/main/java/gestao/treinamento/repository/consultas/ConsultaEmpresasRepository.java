package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.EmpresaConsultaDTO;
import gestao.treinamento.model.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaEmpresasRepository extends JpaRepository<Empresa, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.EmpresaConsultaDTO(e.id, e.nome, e.cnpj, e.cpf) FROM Empresa e")
    List<EmpresaConsultaDTO> findAllEmpresasDTO();
}
