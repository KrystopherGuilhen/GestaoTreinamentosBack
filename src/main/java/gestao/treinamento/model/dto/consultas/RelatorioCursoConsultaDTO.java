package gestao.treinamento.model.dto.consultas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioCursoConsultaDTO {
    // Campos do Curso
    private Long idCurso;
    private String nomeCurso;
    private String conteudoProgramatico;
    private String periodoValidadeCurso;
    private Integer cargaHorariaTotal;

    // Campos da Turma
    private Long idTurma;
    private String nomeTurma;
    private String dataInicio;
    private String dataFim;
    private String nomeCidadeTreinamento;
    private String numeroContrato;
    private Double valorContratoCrm;

    // Campos da Modalidade
    private Long idModalidade;
    private String nomeModalidade;
}
