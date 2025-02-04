package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.MatriculaTrabalhadorVinculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroMatriculaTrabalhadorVinculoRepository extends JpaRepository<MatriculaTrabalhadorVinculo, Long> {

    @Query("SELECT mtv.matriculaTrabalhador.id FROM MatriculaTrabalhadorVinculo mtv WHERE mtv.matriculaTrabalhador.id = :matriculaTrabalhadorId")
    List<Long> findTrabalhadorByMatriculaTrabalhadorId(@Param("matriculaTrabalhadorId") Long matriculaTrabalhadorId);

    boolean existsByMatriculaTrabalhadorIdAndTrabalhadorId(Long matriculaTrabalhadorId, Long trabalhadorId);

    @Modifying
    @Query("DELETE FROM MatriculaTrabalhadorVinculo mtv WHERE mtv.matriculaTrabalhador.id = :matriculaTrabalhadorId AND mtv.trabalhador.id IN :trabalhadoresIds")
    void deleteByMatriculaTrabalhadorIdAndTrabalhadoresIds(@Param("matriculaTrabalhadorId") Long matriculaTrabalhadorId, @Param("trabalhadoresIds") List<Long> trabalhadoresIds);
}
