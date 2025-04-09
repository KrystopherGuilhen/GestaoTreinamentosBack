package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.EstadoConsultaDTO;
import gestao.treinamento.model.entidades.Estados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaEstadosRepository extends JpaRepository<Estados, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.EstadoConsultaDTO(e.id, e.ibgeId, e.ibgeRegiaoId, e.nome, e.sigla) FROM Estados e")
    List<EstadoConsultaDTO> findAllEstadosDTO();
}
