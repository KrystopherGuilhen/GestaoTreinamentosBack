package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoTrabalhadorConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoTrabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoTrabalhadorRepository extends JpaRepository<PermissaoTrabalhador, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoTrabalhadorConsultaDTO(pc.id, pc.nome) FROM PermissaoTrabalhador pc")
    List<PermissaoTrabalhadorConsultaDTO> findAllPermissaoTrabalhadorDTO();
}
