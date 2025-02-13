package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoPerfilConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoPerfilRepository extends JpaRepository<PermissaoPerfil, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoPerfilConsultaDTO(pc.id, pc.nome) FROM PermissaoPerfil pc")
    List<PermissaoPerfilConsultaDTO> findAllPermissaoPerfilDTO();
}
