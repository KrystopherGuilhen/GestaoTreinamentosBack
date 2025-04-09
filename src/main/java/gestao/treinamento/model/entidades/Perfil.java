package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "perfil")
public class Perfil  extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

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

    // Métodos do UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Implemente suas autoridades/roles baseado nas permissões do perfil
        return Collections.emptyList(); // Adapte conforme suas regras
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
