package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "empresa")
public class Empresa {
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

    @Column(name = "cnpj", unique = true, length = 18)
    private String cnpj;

    @Column(name = "cpf", unique = true, length = 11)
    private String cpf;

    @Column(name = "nome_responsavel_empresa", nullable = false, length = 150)
    private String nomeResponsavelEmpresa;

    @Column(name = "email_responsavel_empresa", nullable = false, length = 100)
    private String emailResponsavelEmpresa;

    @Column(name = "relacao_espaco_confinado", length = 100)
    private String relacaoEspacoConfinado;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<EmpresaIndustria> industriasVinculadas = new ArrayList<>();

}