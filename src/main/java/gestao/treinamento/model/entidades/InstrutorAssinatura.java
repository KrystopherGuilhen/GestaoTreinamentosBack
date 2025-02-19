package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "instrutor_assinatura")
public class InstrutorAssinatura {

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
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;

}
