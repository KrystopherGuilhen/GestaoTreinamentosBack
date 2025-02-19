package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "responsavel_tecnico")
public class ResponsavelTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "numero_conselho")
    private Integer numeroConselho;

    @OneToMany(mappedBy = "responsavelTecnico", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ResponsavelTecnicoAssinatura> assinatura = new ArrayList<>();
}