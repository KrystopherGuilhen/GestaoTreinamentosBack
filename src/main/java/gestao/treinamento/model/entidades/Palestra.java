package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "palestra")
public class Palestra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_evento", nullable = false, length = 150)
    private String nomeEvento;

    @Column(name = "cidade_evento", nullable = false, length = 100)
    private String cidadeEvento;

//    @Column(name = "data_inicio", nullable = false)
//    private LocalDate dataInicio;
//
//    @Column(name = "data_fim", nullable = false)
//    private LocalDate dataFim;

//    @Column(name = "valor_contrato_crm", nullable = false)
//    private Double valorContratoCrm;

//    @Column(name = "numero_contrato_crm", nullable = false)
//    private Integer numeroContratoCrm;

    @Column(name = "conteudo_programatico", nullable = false, columnDefinition = "TEXT")
    private String conteudoProgramatico;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

//    @OneToMany(mappedBy = "palestra", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    private List<PalestraEmpresa> empresasVinculadas = new ArrayList<>();
}
