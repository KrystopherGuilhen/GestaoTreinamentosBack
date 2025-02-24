package gestao.treinamento.model.dto.consultas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurmaImpressaoDTO {
    private Long idTurma;
    private String nomeTurma;
    private String dataInicio;
    private String dataFim;
    private String nomeCurso;
    private String modalidade;
    private Integer cargaHoraria;
    private String conteudoProgramaticoCurso;
    private String unidadePolo;
    private String nomeGerenteResponsavel;
    private String nomeInstrutor;
    private String formacaoInstrutor;
    private List<TrabalhadorImpressaoDTO> trabalhadores;
}