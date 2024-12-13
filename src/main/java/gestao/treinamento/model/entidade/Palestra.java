package gestao.treinamento.model.entidade;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "palestra")
public class Palestra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "palestra_id")
    private Long id;

    @Column(name = "nome_evento", nullable = false, length = 150)
    private String nomeEvento;

    @Column(name = "cidade_evento", nullable = false, length = 100)
    private String cidadeEvento;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "valor_contrato_crm", nullable = false)
    private Double valorContratoCrm;

    @Column(name = "numero_contrato_crm", nullable = false)
    private Integer numeroContratoCrm;

    @ManyToMany
    @JoinTable(
            name = "palestra_empresa",
            joinColumns = @JoinColumn(name = "palestra_id"),
            inverseJoinColumns = @JoinColumn(name = "empresa_id")
    )
    private Set<Empresa> empresas;
}
