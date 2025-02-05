//package gestao.treinamento.model.entidades;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "matricula_trabalhador_turma")
//public class MatriculaTrabalhadorTurma {
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
//    @JoinColumn(name = "turma_id", nullable = false)
//    private Turma turma;
//}
