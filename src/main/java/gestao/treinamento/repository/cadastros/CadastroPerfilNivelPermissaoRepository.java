package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilNivelPermissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilNivelPermissaoRepository extends JpaRepository<PerfilNivelPermissao, Long> {

    @Query("SELECT pnp.nivelPermissao.id FROM PerfilNivelPermissao pnp WHERE pnp.perfil.id = :perfilId")
    List<Long> findNivelPermissaoByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndNivelPermissaoId(Long perfilId, Long nivelPermissaoId);

    @Modifying
    @Query("DELETE FROM PerfilNivelPermissao pnp WHERE pnp.perfil.id = :perfilId AND pnp.nivelPermissao.id IN :nivelPermissaoIds")
    void deleteByPerfilIdAndNivelPermissaoIds(@Param("perfilId") Long perfilId, @Param("nivelPermissaoIds") List<Long> nivelPermissaoIds);
}
