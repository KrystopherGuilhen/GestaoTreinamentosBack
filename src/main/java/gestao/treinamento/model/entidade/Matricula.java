//package gestao.treinamento.model.entidade;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import java.time.LocalDate;
//
//@Data
//@Entity
//@Table(name = "matricula")
//public class Matricula {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "matricula_id")
//    private Long id;
//
//    @Column(name = "id_nome_trabalhador")
//    private Long idNomeTrabalhador;
//
//    @Column(name = "nome_trabalhador", nullable = false, length = 150)
//    private String nomeTrabalhador;
//
//    @Column(name = "id_nome_evento_turma")
//    private Long idNomeEventoTurma;
//
//    @Column(name = "nome_evento_turma", nullable = false, length = 150)
//    private String nomeEventoTurma;
//
//    @Column(name = "data_matricula", nullable = false)
//    private LocalDate dataMatricula;
//
//    @ManyToOne
//    @JoinColumn(name = "evento_id", nullable = false)
//    private Palestra evento;
//
//    @ManyToOne
//    @JoinColumn(name = "curso_id", nullable = false)
//    private Curso curso;
//
//    @Column(name = "carga_horaria_total", nullable = false)
//    private Integer cargaHorariaTotal;
//}
