package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidade.PalestraEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CadastroPalestraEmpresaRepository extends JpaRepository<PalestraEmpresa, Long> {

    @Query("SELECT te.empresa.id FROM PalestraEmpresa te WHERE te.palestra.id = :palestraId")
    List<Long> findEmpresasByPalestraId(@Param("palestraId") Long palestraId);

    boolean existsByPalestraIdAndEmpresaId(Long palestraId, Long empresaId);

    @Modifying
    @Query("DELETE FROM PalestraEmpresa te WHERE te.palestra.id = :palestraId AND te.empresa.id IN :empresaIds")
    void deleteByPalestraIdAndEmpresaIds(@Param("palestraId") Long palestraId, @Param("empresaIds") List<Long> empresaIds);
}
