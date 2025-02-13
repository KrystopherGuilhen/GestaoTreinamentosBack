package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoResponsavelTecnicoConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoResponsavelTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoResponsavelTecnicoRepository extends JpaRepository<PermissaoResponsavelTecnico, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoResponsavelTecnicoConsultaDTO(pc.id, pc.nome) FROM PermissaoResponsavelTecnico pc")
    List<PermissaoResponsavelTecnicoConsultaDTO> findAllPermissaoResponsavelTecnicoDTO();
}
