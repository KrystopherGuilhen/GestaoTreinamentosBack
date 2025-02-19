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

    @Query("SELECT new gestao.treinamento.model.dto.consultas.InstrutorFormacaoConsultaDTO(i.id, i.formacao) " +
            "FROM InstrutorFormacao i WHERE i.instrutor.id = :instrutorId")
    List<InstrutorFormacaoConsultaDTO> findInstrutorFormacaosByInstrutorId(@Param("instrutorId") Long instrutorId);
}

