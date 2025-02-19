package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.InstrutorFormacao;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroInstrutorFormacaoRepository extends JpaRepository<InstrutorFormacao, Long> {
    // Buscar formações por ID do Instrutor
    @Query("SELECT f FROM InstrutorFormacao f WHERE f.instrutor.id = :instrutorId")
    List<InstrutorFormacao> findByInstrutorId(Long instrutorId);

    // Excluir formações por ID do Instrutor
    @Modifying
    @Transactional
    @Query("DELETE FROM InstrutorFormacao f WHERE f.instrutor.id = :instrutorId")
    void deleteByInstrutorId(Long instrutorId);

    // Excluir formações por múltiplos IDs de Instrutor
    @Modifying
    @Transactional
    @Query("DELETE FROM InstrutorFormacao f WHERE f.instrutor.id IN :instrutorIds")
    void deleteByInstrutorIdIn(List<Long> instrutorIds);
}
