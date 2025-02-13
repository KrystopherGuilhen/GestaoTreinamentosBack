package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoUnidadeRepository extends JpaRepository<PerfilPermissaoUnidade, Long> {

    @Query("SELECT ppu.permissaoUnidade.id FROM PerfilPermissaoUnidade ppu WHERE ppu.perfil.id = :perfilId")
    List<Long> findPermissaoUnidadeByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoUnidadeId(Long perfilId, Long permissaoUnidadeId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoUnidade ppu WHERE ppu.perfil.id = :perfilId AND ppu.permissaoUnidade.id IN :permissaoUnidadeIds")
    void deleteByPerfilIdAndPermissaoUnidadeIds(@Param("perfilId") Long perfilId, @Param("permissaoUnidadeIds") List<Long> permissaoUnidadeIds);
}
