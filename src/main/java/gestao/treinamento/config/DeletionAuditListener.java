package gestao.treinamento.config;

import gestao.treinamento.model.entidades.DeletionLog;
import gestao.treinamento.model.entidades.Perfil;
import gestao.treinamento.util.ApplicationContextHolder;
import jakarta.persistence.PreRemove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class DeletionAuditListener {

    private static final Logger logger = LoggerFactory.getLogger(DeletionAuditListener.class);

    @PreRemove
    public void preRemove(Object entity) {
        // Obtém o publisher diretamente do ApplicationContext, pois ele implementa ApplicationEventPublisher.
        ApplicationEventPublisher publisher = ApplicationContextHolder.getContext();

        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = "unknown"; // Valor padrão caso não tenha autenticação

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Perfil) {
                Perfil perfil = (Perfil) principal;
                user = perfil.getNome() + " (" + perfil.getEmail() + ")";
            } else {
                user = authentication.getName();
            }
        }

        // Criar log de exclusão
        DeletionLog log = new DeletionLog();
        log.setEntityName(entity.getClass().getSimpleName());
        log.setEntityData(entity.toString());
        log.setDeletedBy(user);
        log.setDeletionTime(LocalDateTime.now());

        // Publica o evento de exclusão (Certifique-se de que a classe EntityDeletedEvent existe)
        publisher.publishEvent(new EntityDeletedEvent(entity, log));

        // Log de auditoria
        logger.info("Registro de exclusão gerado: {} excluído por {} em {}",
                entity.getClass().getSimpleName(), user, log.getDeletionTime());
    }
}