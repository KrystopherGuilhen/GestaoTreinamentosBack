package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilUnidade> perfilUnidadesVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilNivelPermissao> perfilNiveisPermissoesVinculadas = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoCurso> perfilPermissaoCursos = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoEmpresa> perfilPermissaoEmpresas = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoPalestra> perfilPermissaoPalestras = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoPerfil> perfilPermissaoPerfis = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoResponsavelTecnico> perfilPermissaoResponsavelTecnicos = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoTrabalhador> perfilPermissaoTrabalhadores = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoTurma> perfilPermissaoTurmas = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoUnidade> perfilPermissaoUnidades = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PerfilPermissaoInstrutor> perfilPermissaoInstrutores = new ArrayList<>();
}
