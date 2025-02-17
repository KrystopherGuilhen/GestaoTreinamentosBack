package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PessoaConsultaDTO;
import gestao.treinamento.model.entidades.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPessoaRepository extends JpaRepository<Pessoa, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PessoaConsultaDTO(p.id, p.nome) FROM Pessoa p")
    List<PessoaConsultaDTO> findAllPessoasDTO();
}
