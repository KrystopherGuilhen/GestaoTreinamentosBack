package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoTrabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoTrabalhadorRepository extends JpaRepository<PerfilPermissaoTrabalhador, Long> {

    @Query("SELECT ppt.permissaoTrabalhador.id FROM PerfilPermissaoTrabalhador ppt WHERE ppt.perfil.id = :perfilId")
    List<Long> findPermissaoTrabalhadorByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoTrabalhadorId(Long perfilId, Long permissaoTrabalhadorId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoTrabalhador ppt WHERE ppt.perfil.id = :perfilId AND ppt.permissaoTrabalhador.id IN :permissaoTrabalhadorIds")
    void deleteByPerfilIdAndPermissaoTrabalhadorIds(@Param("perfilId") Long perfilId, @Param("permissaoTrabalhadorIds") List<Long> permissaoTrabalhadorIds);
}
