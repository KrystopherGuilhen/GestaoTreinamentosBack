package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.InstrutorCertificados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroInstrutorCertificadosRepository extends JpaRepository<InstrutorCertificados, Long> {

    @Modifying
    @Query("DELETE FROM InstrutorCertificados c WHERE c.instrutor.id = :instrutorId")
    void deleteByInstrutorId(@Param("instrutorId") Long instrutorId);
}
