package gestao.treinamento.config;

import gestao.treinamento.model.entidades.DeletionLog;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EntityDeletedEvent extends ApplicationEvent {
    private final DeletionLog deletionLog;

    public EntityDeletedEvent(Object source, DeletionLog deletionLog) {
        super(source);
        this.deletionLog = deletionLog;
    }
}