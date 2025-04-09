package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "turma")
public class Turma extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "valor_contrato_crm", nullable = false)
    private Double valorContratoCrm;

    @Column(name = "numero_contrato_crm", nullable = false)
    private Integer numeroContratoCrm;

    @Column(name = "id_cidade_treinamento", nullable = false)
    private Long idCidadeTreinamento;

    @Column(name = "nome_cidade_treinamento", nullable = false)
    private String nomeCidadeTreinamento;

    @Column(name = "multiplos_instrutores", nullable = false)
    private Boolean multiplosInstrutores;

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaEvento> turmaEventosVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaModalidade> turmaModalidadesVinculadas = new ArrayList<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaTrabalhador> turmaTrabalhadoresVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaInstrutor> turmaInstrutoresVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaEmpresa> turmaEmpresasVinculadas = new ArrayList<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaCurso> turmaCursosVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaPalestra> turmaPalestrasVinculadas = new ArrayList<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaUnidade> turmaUnidadesVinculadas = new ArrayList<>();

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TurmaInstrutorFormacao> turmaInstrutorFormacaosVinculados = new ArrayList<>();

    @Column(name = "observacao_nr", columnDefinition = "TEXT")
    private String observacaoNr;

}