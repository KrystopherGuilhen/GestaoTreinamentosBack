package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.CidadeConsultaDTO;
import gestao.treinamento.model.entidades.Cidades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaCidadesRepository extends JpaRepository<Cidades, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.CidadeConsultaDTO(c.id, c.estadoId, c.ibgeId, c.nome) " +
            "FROM Cidades c WHERE c.estadoId = :estadoId")
    List<CidadeConsultaDTO> findByIdEstado(@Param("estadoId") Long idEstado);
}
