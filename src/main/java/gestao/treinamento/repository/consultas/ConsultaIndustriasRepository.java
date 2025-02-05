package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.EventoConsultaDTO;
import gestao.treinamento.model.dto.consultas.IndustriaConsultaDTO;
import gestao.treinamento.model.entidades.Industria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaIndustriasRepository extends JpaRepository<Industria, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.IndustriaConsultaDTO(i.id, i.nome) FROM Industria i")
    List<IndustriaConsultaDTO> findAllIndustriasDTO();
}
