package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "curso")
public class Curso extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 250)
    private String nome;

    @Column(name = "conteudo_programatico", nullable = false, columnDefinition = "TEXT")
    private String conteudoProgramatico;

    @Column(name = "carga_horaria_total", nullable = false)
    private Integer cargaHorariaTotal;

    @Column(name = "carga_horaria_ead")
    private Integer cargaHorariaEad;

    @Column(name = "carga_horaria_presencial")
    private Integer cargaHorariaPresencial;

    @Column(name = "periodo_validade_curso")
    private Integer periodoValidade;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<CursoModalidade> modalidadesVinculadas = new ArrayList<>();
}