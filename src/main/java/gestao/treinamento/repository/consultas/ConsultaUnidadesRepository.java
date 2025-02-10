package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.UnidadeConsultaDTO;
import gestao.treinamento.model.entidades.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaUnidadesRepository extends JpaRepository<Unidade, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.UnidadeConsultaDTO(u.id, u.nome) FROM Unidade u")
    List<UnidadeConsultaDTO> findAllUnidadesDTO();
}
