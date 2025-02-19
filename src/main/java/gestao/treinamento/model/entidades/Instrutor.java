package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "experiencia", length = 500)
    private String experiencia;

    @Column(name = "numero_registro_profissional")
    private Integer numeroRegistroProfissional;

    @Column(name = "unidade_registro_profissional", length = 50)
    private String unidadeRegistroProfissional;

    @Column(name = "estado_registro_profissional", length = 2)
    private String estadoRegistroProfissional;

//    @Lob
//    @Column(name = "assinatura", columnDefinition = "LONGBLOB")
//    private byte[] assinatura;

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<InstrutorAssinatura> assinatura = new ArrayList<>();

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<InstrutorPessoa> instrutorPessoaVinculado = new ArrayList<>();

}
