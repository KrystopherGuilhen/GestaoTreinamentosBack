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

    //Turma:
    private String nomeTurma;
    private String dataInicio;
    private String dataFim;
    private String nomeCidadeTreinamento;
    private String observacaoNr;

    //Empresa:
    private String relacaoEspacoConfinado;

    //Palestra:
    private String nomePalestra;
    private Integer cargaHorariaPalestra;
    private String conteudoProgramaticoPalestra;

    //Curso:
    private String nomeCurso;
    private Integer cargaHoraria;
    private String conteudoProgramaticoCurso;
    private Integer periodoValidadeCurso;

    //Modalidade:
    private String modalidade;

    //Evento:
    private Long idEvento;
    private String nomeEvento;

    //Unidade:
    private String unidadePolo;
    private String nomeGerenteResponsavel;
    private String razaoSocialUnidade;
    private String nomeFantasiaUnidade;
    private String cnpjUnidade;
    private String enderecoUnidade;
    private String credenciamentoUnidade;
    private String assinaturaGerenteResponsavel;
    private String nomeResponsavelTecnico;
    private String assinaturaResponsavelTecnico;
    private String numeroConselhoMT;

    //Instrutor:
    private String nomeInstrutor;
    private String nomeInstrutorUm;
    private String nomeInstrutorDois;
    private String formacaoInstrutor;
    private String formacaoInstrutorUm;
    private String formacaoInstrutorDois;
    private String assinaturaInstrutor;

    //Trabalhador
    private List<TrabalhadorImpressaoDTO> trabalhadores;
}