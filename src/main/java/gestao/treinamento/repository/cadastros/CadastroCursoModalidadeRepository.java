package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.CursoModalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroCursoModalidadeRepository extends JpaRepository<CursoModalidade, Long> {

    @Query("SELECT tm.modalidade.id FROM CursoModalidade tm WHERE tm.curso.id = :cursoId")
    List<Long> findModalidadeByCursoId(@Param("cursoId") Long cursoId);

    boolean existsByCursoIdAndModalidadeId(Long cursoId, Long modalidadeId);

    @Modifying
    @Query("DELETE FROM CursoModalidade tm WHERE tm.curso.id = :cursoId AND tm.modalidade.id IN :modalidadeIds")
    void deleteByCursoIdAndModalidadeIds(@Param("cursoId") Long cursoId, @Param("modalidadeIds") List<Long> modalidadeIds);
}
