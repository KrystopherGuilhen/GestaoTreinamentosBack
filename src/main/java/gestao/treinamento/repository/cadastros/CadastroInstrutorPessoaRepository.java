package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.InstrutorPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroInstrutorPessoaRepository extends JpaRepository<InstrutorPessoa, Long> {

    @Query("SELECT ip.pessoa.id FROM InstrutorPessoa ip WHERE ip.instrutor.id = :instrutorId")
    List<Long> findPessoasByInstrutorId(@Param("instrutorId") Long instrutorId);

    boolean existsByInstrutorIdAndPessoaId(Long instrutorId, Long pessoaId);

    @Modifying
    @Query("DELETE FROM InstrutorPessoa ip WHERE ip.instrutor.id = :instrutorId AND ip.pessoa.id IN :pessoaIds")
    void deleteByInstrutorIdAndPessoaIds(@Param("instrutorId") Long instrutorId, @Param("pessoaIds") List<Long> pessoaIds);
}
