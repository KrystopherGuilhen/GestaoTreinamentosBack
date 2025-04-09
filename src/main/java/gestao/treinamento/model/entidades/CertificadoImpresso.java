package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "certificado_impresso")
public class CertificadoImpresso extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Armazena o arquivo PDF como BLOB
    @Lob
    @Column(name = "arquivo", columnDefinition = "LONGBLOB")
    private byte[] arquivo;

    @Column(name = "id_turma")
    private Long idTurma;

    @Column(name = "nome_turma")
    private String nomeTurma;

    @Column(name = "id_evento")
    private Integer idEvento;

    @Column(name = "nome_evento")
    private String nomeEvento;

    @Column(name = "nome_trabalhador")
    private String nomeTrabalhador;

    @Column(name = "cpf_trabalhador")
    private String cpfTrabalhador;

    @Column(name = "rg_trabalhador")
    private String rgTrabalhador;

    @Column(name = "nome_curso")
    private String nomeCurso;

    @Column(name = "modalidade")
    private String modalidade;

    @Column(name = "data_inicio")
    private String dataInicio;

    @Column(name = "data_fim")
    private String dataFim;

    @Column(name = "data_expiracao")
    private String dataExpiracao;

    @Column(name = "nome_cidade_treinamento")
    private String nomeCidadeTreinamento;

    @Column(name = "data_emissao")
    private String dataEmissao;

    @Column(name = "nome_Palestra")
    private String nomePalestra;

    @Column(name = "unidade_Polo")
    private String unidadePolo;

    @Column(name = "nome_Instrutor")
    private String nomeInstrutor;
}
