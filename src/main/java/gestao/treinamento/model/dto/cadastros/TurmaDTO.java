package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurmaDTO {

    private Long id;
    private String nome;
    private String dataInicio;
    private String dataFim;
    private Double valorContratoCrm;

    private Long idEventoVinculo;
    private List<String> nomeEventoVinculo;

    private Long idModalidadeVinculo;
    private List<String> nomeModalidadeVinculo;

    private List<Long> idTrabalhadorVinculo;
    private List<String> nomeTrabalhadorVinculo;

    private Long idInstrutorVinculo;
    private List<String> nomeInstrutorVinculo;

    private List<Long> idEmpresaVinculo;
    private List<String> nomeEmpresaVinculo;

    private Long idCursoVinculo;
    private List<String> nomeCursoVinculo;

    private String observacaoNr33;
}
