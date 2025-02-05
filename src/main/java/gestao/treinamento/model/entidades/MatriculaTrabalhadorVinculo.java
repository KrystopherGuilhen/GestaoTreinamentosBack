//package gestao.treinamento.model.entidades;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "matricula_trabalhador_vinculo")
//public class MatriculaTrabalhadorVinculo {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "matriculaTrabalhador_id", nullable = false)
//    private MatriculaTrabalhador matriculaTrabalhador;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "trabalhador_id", nullable = false)
//    private Trabalhador trabalhador;
//}
