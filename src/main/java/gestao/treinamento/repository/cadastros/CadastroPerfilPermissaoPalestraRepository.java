package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoPalestra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoPalestraRepository extends JpaRepository<PerfilPermissaoPalestra, Long> {

    @Query("SELECT ppp.permissaoPalestra.id FROM PerfilPermissaoPalestra ppp WHERE ppp.perfil.id = :perfilId")
    List<Long> findPermissaoPalestraByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoPalestraId(Long perfilId, Long permissaoPalestraId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoPalestra ppp WHERE ppp.perfil.id = :perfilId AND ppp.permissaoPalestra.id IN :permissaoPalestraIds")
    void deleteByPerfilIdAndPermissaoPalestraIds(@Param("perfilId") Long perfilId, @Param("permissaoPalestraIds") List<Long> permissaoPalestraIds);
}
