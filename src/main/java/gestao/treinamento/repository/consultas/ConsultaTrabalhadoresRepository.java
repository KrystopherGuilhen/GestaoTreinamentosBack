package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.TrabalhadorConsultaDTO;
import gestao.treinamento.model.entidades.Trabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaTrabalhadoresRepository extends JpaRepository<Trabalhador, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.TrabalhadorConsultaDTO(t.id, t.nome, t.cpf) FROM Trabalhador t")
    List<TrabalhadorConsultaDTO> findAllTrabalhadoresDTO();

    @Query("SELECT new gestao.treinamento.model.dto.consultas.TrabalhadorConsultaDTO(t.id, t.nome, t.cpf) " +
            "FROM Trabalhador t " +
            "JOIN TrabalhadorEmpresa te ON t.id = te.trabalhador.id " +
            "WHERE te.empresa.id IN :empresaIds")
    List<TrabalhadorConsultaDTO> findAllTrabalhadoresByEmpresaIds(@Param("empresaIds") List<Long> empresaIds);

    @Query("SELECT new gestao.treinamento.model.dto.consultas.TrabalhadorConsultaDTO(t.id, t.nome, t.cpf) " +
            "FROM Trabalhador t " +
            "JOIN TurmaTrabalhador tt ON t.id = tt.trabalhador.id " +
            "WHERE tt.turma.id = :turmaId")
    List<TrabalhadorConsultaDTO> findAllTrabalhadoresByTurmaId(@Param("turmaId") Long turmaId);
}