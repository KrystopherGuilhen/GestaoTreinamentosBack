package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoInstrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoInstrutorRepository extends JpaRepository<PerfilPermissaoInstrutor, Long> {

    @Query("SELECT ppi.permissaoInstrutor.id FROM PerfilPermissaoInstrutor ppi WHERE ppi.perfil.id = :perfilId")
    List<Long> findPermissaoInstrutorByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoInstrutorId(Long perfilId, Long permissaoInstrutorId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoInstrutor ppi WHERE ppi.perfil.id = :perfilId AND ppi.permissaoInstrutor.id IN :permissaoInstrutorIds")
    void deleteByPerfilIdAndPermissaoInstrutorIds(@Param("perfilId") Long perfilId, @Param("permissaoInstrutorIds") List<Long> permissaoInstrutorIds);
}
