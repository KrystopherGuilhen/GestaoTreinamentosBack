package gestao.treinamento.service;


import gestao.treinamento.config.EntityDeletedEvent;
import gestao.treinamento.model.entidades.DeletionLog;
import gestao.treinamento.repository.DeletionLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class DeletionLogService {

    private final DeletionLogRepository deletionLogRepository;

    public DeletionLogService(DeletionLogRepository deletionLogRepository) {
        this.deletionLogRepository = deletionLogRepository;
    }

    @EventListener
    public void handleEntityDeleted(EntityDeletedEvent event) {
        DeletionLog log = event.getDeletionLog();
        deletionLogRepository.save(log);
    }
}