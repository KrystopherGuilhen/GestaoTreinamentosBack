package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.EmpresaIndustria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroEmpresaIndustriaRepository extends JpaRepository<EmpresaIndustria, Long> {

    @Query("SELECT ei.industria.id FROM EmpresaIndustria ei WHERE ei.empresa.id = :empresaId")
    List<Long> findIndustriaByEmpresaId(@Param("empresaId") Long empresaId);

    boolean existsByEmpresaIdAndIndustriaId(Long empresaId, Long industriaId);

    @Modifying
    @Query("DELETE FROM EmpresaIndustria ei WHERE ei.empresa.id = :empresaId AND ei.industria.id IN :industriaIds")
    void deleteByEmpresaIdAndIndustriaIds(@Param("empresaId") Long empresaId, @Param("industriaIds") List<Long> industriaIds);
}
