package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "matricula_trabalhador")
public class MatriculaTrabalhador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "matriculaTrabalhador", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MatriculaTrabalhadorVinculo> matriculaTrabalhadoresVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "matriculaTrabalhador", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MatriculaTrabalhadorTurma> matriculaTrabalhadorTurmasVinculadas = new ArrayList<>();

    @OneToMany(mappedBy = "matriculaTrabalhador", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MatriculaTrabalhadorCurso> matriculaTrabalhadorCursosVinculados = new ArrayList<>();

    @Column(name = "carga_horaria_total", nullable = false)
    private Integer cargaHorariaTotal;
}
