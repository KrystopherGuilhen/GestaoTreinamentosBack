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
@Table(name = "empresa")
public class Empresa extends Auditable {
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
    private String NomeCidade;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cpf", unique = true, nullable = true, length = 11)
    private String cpf;

    @Column(name = "cnpj", unique = true, nullable = true, length = 18)
    private String cnpj;

    @Column(name = "nome_responsavel_empresa", nullable = false, length = 150)
    private String nomeResponsavelEmpresa;

    @Column(name = "email_responsavel_empresa", nullable = false, length = 100)
    private String emailResponsavelEmpresa;

    @Column(name = "relacao_espaco_confinado", columnDefinition = "TEXT")
    private String relacaoEspacoConfinado;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<EmpresaIndustria> industriasVinculadas = new ArrayList<>();

}