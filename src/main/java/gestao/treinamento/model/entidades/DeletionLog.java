package gestao.treinamento.model.entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "deletion_log")
public class DeletionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "entity_data", columnDefinition = "LONGTEXT")
    private String entityData;

    @Column(name = "deleted_by", nullable = false)
    private String deletedBy;

    @Column(name = "deletion_time", nullable = false)
    private LocalDateTime deletionTime;
}