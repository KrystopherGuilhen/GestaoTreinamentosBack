package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "unidade_assinatura")
public class UnidadeAssinatura extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_url")
    private String objectURL;

    @Column(name = "nome_arquivo", nullable = false)
    private String name;

    @Column(name = "tipo_mime", nullable = false)
    private String mimeType;

    @Column(name = "tipo_arquivo")
    private String type;

    @Column(name = "tamanho_arquivo")
    private Long size;

    @Lob
    @Column(name = "dados", columnDefinition = "LONGBLOB")
    private byte[] dados;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;
}
