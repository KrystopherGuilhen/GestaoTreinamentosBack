package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "instrutor")
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "formacao", length = 100)
    private String formacao;

    @Column(name = "numero_registro_profissional", nullable = false)
    private Integer numeroRegistroProfissional;

    @Column(name = "unidade_registro_profissional", length = 50)
    private String unidadeRegistroProfissional;

    @Column(name = "estado_registro_profissional", length = 2)
    private String estadoRegistroProfissional;

}
