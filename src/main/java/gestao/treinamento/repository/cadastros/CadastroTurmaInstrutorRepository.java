package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.TurmaInstrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaInstrutorRepository extends JpaRepository<TurmaInstrutor, Long> {

    // Buscar vínculos por turma e tipo
    List<TurmaInstrutor> findByTurmaIdAndIdMultiploInstrutor(Long turmaId, int tipo);

    // Verificar se um vínculo específico já existe
    boolean existsByTurmaIdAndInstrutorIdAndIdMultiploInstrutor(
            Long turmaId,
            Long instrutorId,
            int tipo
    );

    @Query("SELECT ti.instrutor.id FROM TurmaInstrutor ti WHERE ti.turma.id = :turmaId")
    List<Long> findInstrutorByTurmaId(@Param("turmaId") Long turmaId);

    boolean existsByTurmaIdAndInstrutorId(Long turmaId, Long instrutorId);

    @Modifying
    @Query("DELETE FROM TurmaInstrutor ti WHERE ti.turma.id = :turmaId AND ti.instrutor.id IN :instrutorIds")
    void deleteByTurmaIdAndInstrutorIds(@Param("turmaId") Long turmaId, @Param("instrutorIds") List<Long> instrutorIds);

    @Query("SELECT ti FROM TurmaInstrutor ti WHERE ti.turma.id = :turmaId AND ti.idMultiploInstrutor IN :tipos")
    List<TurmaInstrutor> findByTurmaIdAndIdMultiploInstrutorIn(
            @Param("turmaId") Long turmaId,
            @Param("tipos") List<Integer> tipos
    );
}
