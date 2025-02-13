package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "perfil_permissao_curso")
public class PerfilPermissaoCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permissao_curso_id", nullable = false)
    private PermissaoCurso permissaoCurso;
}
