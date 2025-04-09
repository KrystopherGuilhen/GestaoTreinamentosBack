package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "instrutor_certificados")
public class InstrutorCertificados extends Auditable {

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
    private String type; // Campo opcional

    @Column(name = "tamanho_arquivo")
    private Long size; // Tamanho em bytes

    @Lob
    @Column(name = "dados", columnDefinition = "LONGBLOB")
    private byte[] dados;

    // Relacionamento com Instrutor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;
}
