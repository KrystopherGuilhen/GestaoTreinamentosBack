package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidade.CursoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CadastroCursoEmpresaRepository extends JpaRepository<CursoEmpresa, Long> {

    @Query("SELECT te.empresa.id FROM CursoEmpresa te WHERE te.curso.id = :cursoId")
    List<Long> findEmpresasByCursoId(@Param("cursoId") Long cursoId);

    boolean existsByCursoIdAndEmpresaId(Long cursoId, Long empresaId);

    @Modifying
    @Query("DELETE FROM CursoEmpresa te WHERE te.curso.id = :cursoId AND te.empresa.id IN :empresaIds")
    void deleteByCursoIdAndEmpresaIds(@Param("cursoId") Long cursoId, @Param("empresaIds") List<Long> empresaIds);
}
