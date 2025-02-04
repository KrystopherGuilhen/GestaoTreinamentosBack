package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.ModalidadeConsultaDTO;
import gestao.treinamento.model.entidades.Modalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaModalidadesRepository extends JpaRepository<Modalidade, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.ModalidadeConsultaDTO(m.id, m.nome) FROM Modalidade m")
    List<ModalidadeConsultaDTO> findAllModalidadesDTO();
}
