package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoPerfilRepository extends JpaRepository<PerfilPermissaoPerfil, Long> {

    @Query("SELECT ppp.permissaoPerfil.id FROM PerfilPermissaoPerfil ppp WHERE ppp.perfil.id = :perfilId")
    List<Long> findPermissaoPerfilByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoPerfilId(Long perfilId, Long permissaoPerfilId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoPerfil ppp WHERE ppp.perfil.id = :perfilId AND ppp.permissaoPerfil.id IN :permissaoPerfilIds")
    void deleteByPerfilIdAndPermissaoPerfilIds(@Param("perfilId") Long perfilId, @Param("permissaoPerfilIds") List<Long> permissaoPerfilIds);
}
