package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificadoImpressoDTO {

    private Long id;
    private byte[] arquivo;
    private Long idTurma;
    private String nomeTurma;
    private Integer idEvento;
    private String nomeEvento;
    private String nomeTrabalhador;
    private String cpfTrabalhador;
    private String rgTrabalhador;
    private String nomeCurso;
    private String modalidadeCurso;
    private String dataInicio;
    private String dataFim;
    private String dataExpiracao;
    private String nomeCidadeTreinamento;
    private String dataEmissao;
    private String nomePalestra;
    private String unidadePolo;
    private String nomeInstrutor;
}