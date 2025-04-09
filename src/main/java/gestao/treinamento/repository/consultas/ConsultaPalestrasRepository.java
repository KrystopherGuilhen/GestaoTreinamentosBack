package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PalestraConsultaDTO;
import gestao.treinamento.model.entidades.Palestra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPalestrasRepository extends JpaRepository<Palestra, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PalestraConsultaDTO(p.id, p.nomeEvento) FROM Palestra p")
    List<PalestraConsultaDTO> findAllPalestrasDTO();
}
