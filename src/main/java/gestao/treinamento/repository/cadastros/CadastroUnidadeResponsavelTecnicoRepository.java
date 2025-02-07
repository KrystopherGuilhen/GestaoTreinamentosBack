package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.UnidadeResponsavelTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroUnidadeResponsavelTecnicoRepository extends JpaRepository<UnidadeResponsavelTecnico, Long> {

    @Query("SELECT urt.responsavelTecnico.id FROM UnidadeResponsavelTecnico urt WHERE urt.unidade.id = :unidadeId")
    List<Long> findResponsavelTecnicoByUnidadeId(@Param("unidadeId") Long unidadeId);

    boolean existsByUnidadeIdAndResponsavelTecnicoId(Long unidadeId, Long responsavelTecnicoId);

    @Modifying
    @Query("DELETE FROM UnidadeResponsavelTecnico urt WHERE urt.unidade.id = :unidadeId AND urt.responsavelTecnico.id IN :responsavelTecnicoIds")
    void deleteByUnidadeIdAndResponsavelTecnicoIds(@Param("unidadeId") Long unidadeId, @Param("responsavelTecnicoIds") List<Long> responsavelTecnicoIds);
}
