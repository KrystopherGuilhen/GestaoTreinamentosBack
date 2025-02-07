package gestao.treinamento.repository.cadastros;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilUnidadeRepository {

    @Query("SELECT tm.unidade.id FROM PerfilUnidade tm WHERE tm.perfil.id = :perfilId")
    List<Long> findUnidadeByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndUnidadeId(Long perfilId, Long unidadeId);

    @Modifying
    @Query("DELETE FROM PerfilUnidade tm WHERE tm.perfil.id = :perfilId AND tm.unidade.id IN :unidadeIds")
    void deleteByPerfilIdAndUnidadeIds(@Param("perfilId") Long perfilId, @Param("unidadeIds") List<Long> unidadeIds);
}
