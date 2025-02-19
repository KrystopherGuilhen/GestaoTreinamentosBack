package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.UnidadeAssinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroUnidadeAssinaturaRepository extends JpaRepository<UnidadeAssinatura, Long> {

    @Modifying
    @Query("DELETE FROM UnidadeAssinatura c WHERE c.unidade.id = :unidadeId")
    void deleteByUnidadeId(@Param("unidadeId") Long unidadeId);
}
