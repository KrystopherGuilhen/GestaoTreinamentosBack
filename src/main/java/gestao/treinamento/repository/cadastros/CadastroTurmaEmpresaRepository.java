package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.TurmaEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaEmpresaRepository extends JpaRepository<TurmaEmpresa, Long> {

    @Query("SELECT te.empresa.id FROM TurmaEmpresa te WHERE te.turma.id = :turmaId")
    List<Long> findEmpresaByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndEmpresaId(Long turmaId, Long empresaId);

    @Modifying
    @Query("DELETE FROM TurmaEmpresa te WHERE te.turma.id = :turmaId AND te.empresa.id IN :empresaIds")
    void deleteByTurmaIdAndEmpresaIds(@Param("turmaId") Long turmaId, @Param("empresaIds") List<Long> empresaIds);
}
