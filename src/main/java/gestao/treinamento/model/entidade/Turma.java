package gestao.treinamento.model.entidade;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

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

//    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    private List<CursoEmpresa> empresasVinculadas = new ArrayList<>();

}
