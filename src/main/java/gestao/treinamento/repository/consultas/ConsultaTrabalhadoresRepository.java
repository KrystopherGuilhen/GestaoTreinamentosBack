package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.TrabalhadorConsultaDTO;
import gestao.treinamento.model.entidades.Trabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaTrabalhadoresRepository extends JpaRepository<Trabalhador, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.TrabalhadorConsultaDTO(t.id, t.nome, t.cpf) FROM Trabalhador t")
    List<TrabalhadorConsultaDTO> findAllTrabalhadoresDTO();
}
