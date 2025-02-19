package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.ResponsavelTecnicoAssinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroResponsavelTecnicoAssinaturaRepository extends JpaRepository<ResponsavelTecnicoAssinatura, Long> {

    @Modifying
    @Query("DELETE FROM ResponsavelTecnicoAssinatura c WHERE c.responsavelTecnico.id = :responsavelTecnicoId")
    void deleteByResponsavelTecnicoId(@Param("responsavelTecnicoId") Long responsavelTecnicoId);
}
