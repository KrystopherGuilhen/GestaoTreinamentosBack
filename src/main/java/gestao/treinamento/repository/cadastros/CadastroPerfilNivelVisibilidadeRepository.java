package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilNivelVisibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilNivelVisibilidadeRepository extends JpaRepository<PerfilNivelVisibilidade, Long> {

    @Query("SELECT pnv.nivelVisibilidade.id FROM PerfilNivelVisibilidade pnv WHERE pnv.perfil.id = :perfilId")
    List<Long> findNivelVisibilidadeByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndNivelVisibilidadeId(Long perfilId, Long nivelVisibilidadeId);

    @Modifying
    @Query("DELETE FROM PerfilNivelVisibilidade pnv WHERE pnv.perfil.id = :perfilId AND pnv.nivelVisibilidade.id IN :nivelVisibilidadeIds")
    void deleteByPerfilIdAndNivelVisibilidadeIds(@Param("perfilId") Long perfilId, @Param("nivelVisibilidadeIds") List<Long> nivelVisibilidadeIds);
}
