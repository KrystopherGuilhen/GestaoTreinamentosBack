package gestao.treinamento.model.entidade;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "trabalhadores")
public class Trabalhadores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, length = 20)
    private String rg;

    @Column(name = "dataNasc", nullable = false)
    private LocalDate dataNasc;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @ElementCollection
    @CollectionTable(
            name = "empresaVinculoIds",
            joinColumns = @JoinColumn(name = "trabalhador_id")
    )
    @Column(name = "idEmpresaVinculo")
    private List<Long> idEmpresaVinculo;

    @ElementCollection
    @CollectionTable(
            name = "empresaVinculoNomes",
            joinColumns = @JoinColumn(name = "trabalhador_id")
    )
    @Column(name = "nomeEmpresaVinculo")
    private List<String> nomeEmpresaVinculo;
}
