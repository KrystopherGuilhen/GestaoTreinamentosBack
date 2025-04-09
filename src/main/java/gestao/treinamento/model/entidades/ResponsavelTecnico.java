package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "responsavel_tecnico")
public class ResponsavelTecnico extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "numero_conselho")
    private String numeroConselho;

    @OneToMany(mappedBy = "responsavelTecnico", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ResponsavelTecnicoAssinatura> assinatura = new ArrayList<>();
}