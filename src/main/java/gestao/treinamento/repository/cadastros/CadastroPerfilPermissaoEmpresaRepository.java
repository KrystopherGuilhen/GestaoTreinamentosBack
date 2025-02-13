package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoEmpresaRepository extends JpaRepository<PerfilPermissaoEmpresa, Long> {

    @Query("SELECT ppe.permissaoEmpresa.id FROM PerfilPermissaoEmpresa ppe WHERE ppe.perfil.id = :perfilId")
    List<Long> findPermissaoEmpresaByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoEmpresaId(Long perfilId, Long permissaoEmpresaId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoEmpresa ppe WHERE ppe.perfil.id = :perfilId AND ppe.permissaoEmpresa.id IN :permissaoEmpresaIds")
    void deleteByPerfilIdAndPermissaoEmpresaIds(@Param("perfilId") Long perfilId, @Param("permissaoEmpresaIds") List<Long> permissaoEmpresaIds);
}
