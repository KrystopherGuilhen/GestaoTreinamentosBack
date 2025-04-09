package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.CursoConsultaDTO;
import gestao.treinamento.model.dto.consultas.RelatorioCursoConsultaDTO;
import gestao.treinamento.model.entidades.Curso;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaCursosRepository extends JpaRepository<Curso, Long> {

    @Query("SELECT DISTINCT new gestao.treinamento.model.dto.consultas.CursoConsultaDTO(c.id, c.nome, m.nome, c.cargaHorariaTotal) " +
            "FROM Curso c " +
            "LEFT JOIN c.modalidadesVinculadas cm " +
            "LEFT JOIN cm.modalidade m")
    List<CursoConsultaDTO> findAllCursosDTO();

    @Query(value = "SELECT " +
            "c.id AS idCurso, c.nome AS nomeCurso, c.conteudo_programatico AS conteudoProgramatico, " +
            "CAST(c.periodo_validade_curso AS CHAR) AS periodoValidadeCurso, c.carga_horaria_total AS cargaHorariaTotal, " +
            "t.id AS idTurma, t.nome AS nomeTurma, " +
            "DATE_FORMAT(t.data_inicio, '%d/%m/%Y') AS dataInicio, " + // Formatação no MySQL
            "DATE_FORMAT(t.data_fim, '%d/%m/%Y') AS dataFim, " +
            "t.nome_cidade_treinamento AS nomeCidadeTreinamento, " +
            "CAST(t.numero_contrato_crm AS CHAR) AS numeroContrato, " +
            "t.valor_contrato_crm AS valorContratoCrm, " +
            "m.id AS idModalidade, m.nome AS nomeModalidade " +
            "FROM turma_curso tc " +
            "JOIN turma t ON tc.turma_id = t.id " +
            "JOIN curso c ON tc.curso_id = c.id " +
            "JOIN turma_modalidade tm ON t.id = tm.turma_id " +
            "JOIN modalidade m ON tm.modalidade_id = m.id " +
            "WHERE t.ativo = 0 AND t.data_fim < CURDATE()", nativeQuery = true)
    List<Tuple> findRelatorioCursos();
}