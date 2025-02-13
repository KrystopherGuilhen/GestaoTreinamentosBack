package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "permissao_palestra")
public class PermissaoPalestra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;
}
