//package gestao.treinamento.model.entidade;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.ToString;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@Entity
//@Table(name = "matricula_trabalhador")
//public class MatriculaTrabalhador {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @OneToMany(mappedBy = "matricula_trabalhador", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    private List<MatriculaTrabalhadorVinculo> matriculaTrabalhadorVinculos = new ArrayList<>();
//
//    @Column(name = "carga_horaria_total", nullable = false)
//    private Integer cargaHorariaTotal;
//}
