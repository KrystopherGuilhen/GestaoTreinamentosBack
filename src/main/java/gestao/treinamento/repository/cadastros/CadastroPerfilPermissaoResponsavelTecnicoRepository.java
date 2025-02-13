package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoResponsavelTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoResponsavelTecnicoRepository extends JpaRepository<PerfilPermissaoResponsavelTecnico, Long> {

    @Query("SELECT pprt.permissaoResponsavelTecnico.id FROM PerfilPermissaoResponsavelTecnico pprt WHERE pprt.perfil.id = :perfilId")
    List<Long> findPermissaoResponsavelTecnicoByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoResponsavelTecnicoId(Long perfilId, Long permissaoResponsavelTecnicoId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoResponsavelTecnico pprt WHERE pprt.perfil.id = :perfilId AND pprt.permissaoResponsavelTecnico.id IN :permissaoResponsavelTecnicoIds")
    void deleteByPerfilIdAndPermissaoResponsavelTecnicoIds(@Param("perfilId") Long perfilId, @Param("permissaoResponsavelTecnicoIds") List<Long> permissaoResponsavelTecnicoIds);
}
