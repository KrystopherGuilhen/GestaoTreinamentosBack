package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.InstrutorFormacaoConsultaDTO;
import gestao.treinamento.model.entidades.InstrutorFormacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaInstrutorFormacaoRepository extends JpaRepository<InstrutorFormacao, Long> {

    @Query("SELECT i.instrutor.id AS instrutorId, i.id AS formacaoId, i.formacao AS formacaoNome " +
            "FROM InstrutorFormacao i " +
            "WHERE i.instrutor.id IN :instrutorIds")
    List<Object[]> findFormacoesAgrupadas(@Param("instrutorIds") List<Long> instrutorIds);
}