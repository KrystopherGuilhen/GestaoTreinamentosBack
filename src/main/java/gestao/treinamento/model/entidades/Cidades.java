package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cidades")
public class Cidades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "estado_id", nullable = false, length = 150)
    private Long estadoId;

    @Column(name = "ibge_id", nullable = false, length = 150)
    private Long ibgeId;

    @Column(name = "nome", nullable = false)
    private String nome;
}
