package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoEmpresaConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoEmpresaRepository extends JpaRepository<PermissaoEmpresa, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoEmpresaConsultaDTO(pc.id, pc.nome) FROM PermissaoEmpresa pc")
    List<PermissaoEmpresaConsultaDTO> findAllPermissaoEmpresaDTO();
}
