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
@Table(name = "instrutor")
public class Instrutor extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "id_estado", nullable = false)
    private Long idEstado;

    @Column(name = "nome_estado", nullable = false, length = 2)
    private String nomeEstado;

    @Column(name = "id_cidade", nullable = false)
    private Long idCidade;

    @Column(name = "nome_cidade", nullable = false, length = 100)
    private String nomeCidade;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cpf", unique = true, nullable = true, length = 11)
    private String cpf;

    @Column(name = "cnpj", unique = true, nullable = true, length = 18)
    private String cnpj;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<InstrutorCertificados> certificados = new ArrayList<>();

    @Column(name = "possui_multiplas_formacoes")
    private boolean possuiMultiplasFormacoes;

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<InstrutorFormacao> formacoes = new ArrayList<>();

    @Column(name = "experiencia", columnDefinition = "TEXT")
    private String experiencia;

    @Column(name = "numero_registro_profissional")
    private Integer numeroRegistroProfissional;

    @Column(name = "unidade_registro_profissional", length = 50)
    private String unidadeRegistroProfissional;

    @Column(name = "estado_registro_profissional", length = 2)
    private String estadoRegistroProfissional;

    @Column(name = "id_estado_registro_profissional", nullable = false)
    private Long idEstadoRegistroProfissional;

    @Column(name = "nome_estado_registro_profissional", nullable = false, length = 2)
    private String nomeEstadoRegistroProfissional;

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<InstrutorAssinatura> assinatura = new ArrayList<>();

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<InstrutorPessoa> instrutorPessoaVinculado = new ArrayList<>();

}
