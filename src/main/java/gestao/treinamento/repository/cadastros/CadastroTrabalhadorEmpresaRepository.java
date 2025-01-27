package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidade.TrabalhadorEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CadastroTrabalhadorEmpresaRepository extends JpaRepository<TrabalhadorEmpresa, Long> {

    @Query("SELECT te.empresa.id FROM TrabalhadorEmpresa te WHERE te.trabalhador.id = :trabalhadorId")
    List<Long> findEmpresasByTrabalhadorId(@Param("trabalhadorId") Long trabalhadorId);

    boolean existsByTrabalhadorIdAndEmpresaId(Long trabalhadorId, Long empresaId);

    @Modifying
    @Query("DELETE FROM TrabalhadorEmpresa te WHERE te.trabalhador.id = :trabalhadorId AND te.empresa.id IN :empresaIds")
    void deleteByTrabalhadorIdAndEmpresaIds(@Param("trabalhadorId") Long trabalhadorId, @Param("empresaIds") List<Long> empresaIds);

}
