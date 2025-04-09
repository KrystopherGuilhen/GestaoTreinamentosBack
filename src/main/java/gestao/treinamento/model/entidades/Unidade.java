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
@Table(name = "unidade")
public class Unidade extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "gerente_responsavel", nullable = false, length = 100)
    private String gerenteResponsavel;

    @Column(name = "razao_social", nullable = false, length = 100)
    private String razaoSocial;

    @Column(name = "nome_fantasia", nullable = false, length = 100)
    private String nomeFantasia;

    @Column(name = "cnpj", unique = true, nullable = true, length = 18)
    private String cnpj;

    @Column(name = "endereco", nullable = false, length = 100)
    private String endereco;

    @Column(name = "credenciamento", nullable = true, length = 100)
    private String credenciamento;


//    @Column(name = "responsavel_tecnico", nullable = false, length = 100)
//    private String responsavelTecnico;

//    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    private List<UnidadePerfil> unidadePerfilsVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UnidadeResponsavelTecnico> unidadeResponsavelTecnicosVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UnidadeAssinatura> assinatura = new ArrayList<>();
}
