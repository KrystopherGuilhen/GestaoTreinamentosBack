package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "trabalhador")
public class Trabalhador extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "id_estado", nullable = true)
    private Long idEstado;

    @Column(name = "nome_estado", nullable = false, length = 2)
    private String nomeEstado;

    @Column(name = "id_cidade", nullable = true)
    private Long idCidade;

    @Column(name = "nome_cidade", nullable = false, length = 100)
    private String NomeCidade;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "rg", nullable = false, unique = true, length = 12)
    private String rg;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @OneToMany(mappedBy = "trabalhador", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TrabalhadorEmpresa> empresasVinculadas = new ArrayList<>();

}