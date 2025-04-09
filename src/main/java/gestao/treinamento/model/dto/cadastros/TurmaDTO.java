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
    private Boolean ativo;
    private String nome;
    private String dataInicio;
    private String dataFim;
    private Double valorContratoCrm;
    private Integer numeroContratoCrm;

    private Long idEventoVinculo;
    private String nomeEventoVinculo;

    private Long idModalidadeVinculo;
    private String nomeModalidadeVinculo;

    private Long idUnidadeVinculo;
    private String nomeUnidadeVinculo;

    private Long idCidadeTreinamento;
    private String nomeCidadeTreinamento;

    private List<Long> idTrabalhadorVinculo;
    private List<String> nomeTrabalhadorVinculo;

    private Long idInstrutorVinculo;
    private String nomeInstrutorVinculo;

    private Long idInstrutorFormacaoVinculo;
    private String nomeInstrutorFormacaoVinculo;

    private Long idInstrutorVinculoUm;
    private String nomeInstrutorVinculoUm;

    private Long idInstrutorFormacaoVinculoUm;
    private String nomeInstrutorFormacaoVinculoUm;

    private Long idInstrutorVinculoDois;
    private String nomeInstrutorVinculoDois;

    private Long idInstrutorFormacaoVinculoDois;
    private String nomeInstrutorFormacaoVinculoDois;

    private List<Long> idEmpresaVinculo;
    private List<String> nomeEmpresaVinculo;

    private Long idCursoVinculo;
    private String nomeCursoVinculo;

    private Long idPalestraVinculo;
    private String nomePalestraVinculo;

    private String observacaoNr;
    private Boolean multiplosInstrutores;

    private Boolean dataFimPassada; // Novo campo calculado
    private String mensagemInativacao; // Novo campo calculado
}
