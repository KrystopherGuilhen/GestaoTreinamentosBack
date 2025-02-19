package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "unidade")
public class Unidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "gerente_responsavel", nullable = false, length = 100)
    private String gerenteResponsavel;
//
//    @Column(name = "responsavel_tecnico", nullable = false, length = 100)
//    private String responsavelTecnico;

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UnidadePerfil> unidadePerfilsVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UnidadeResponsavelTecnico> unidadeResponsavelTecnicosVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UnidadeAssinatura> assinatura = new ArrayList<>();
}
