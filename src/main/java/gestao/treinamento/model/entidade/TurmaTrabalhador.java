package gestao.treinamento.model.entidade;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "turma_trabalhador")
public class TurmaTrabalhador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trabalhador_id", nullable = false)
    private Trabalhador trabalhador;
}
