package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "palestra")
public class Palestra extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_evento", nullable = false, length = 150)
    private String nomeEvento;

    @Column(name = "carga_horaria_total", nullable = false)
    private Integer cargaHorariaTotal;

    @Column(name = "conteudo_programatico", nullable = false, columnDefinition = "TEXT")
    private String conteudoProgramatico;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

}
