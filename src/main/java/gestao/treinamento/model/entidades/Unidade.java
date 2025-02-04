package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "unidade")
public class Unidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_unidade", nullable = false, length = 150)
    private String nomeUnidade;

    @Column(name = "gerente_responsavel", nullable = false, length = 100)
    private String gerenteResponsavel;

    @Column(name = "responsavel_tecnico", nullable = false, length = 100)
    private String responsavelTecnico;
}
