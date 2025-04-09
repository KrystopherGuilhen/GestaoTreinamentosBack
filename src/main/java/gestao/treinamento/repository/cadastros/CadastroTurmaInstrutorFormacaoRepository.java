package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.TurmaInstrutorFormacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroTurmaInstrutorFormacaoRepository extends JpaRepository<TurmaInstrutorFormacao, Long> {

    // CORRETO: Usa o nome do campo idMultiploFormacao
    List<TurmaInstrutorFormacao> findByTurmaIdAndIdMultiploFormacao(Long turmaId, int tipo);

    // CORRETO: Usa instrutorFormacao.id e idMultiploFormacao
    boolean existsByTurmaIdAndInstrutorFormacaoIdAndIdMultiploFormacao(
            Long turmaId,
            Long instrutorFormacaoId,
            int tipo
    );
}
