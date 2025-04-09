package gestao.treinamento.config;

import gestao.treinamento.model.entidades.Perfil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Perfil) {
            Perfil perfil = (Perfil) principal;
            // Concatena o nome e o e-mail. Você pode ajustar o formato conforme sua necessidade.
            String auditorInfo = perfil.getNome() + " (" + perfil.getEmail() + ")";
            return Optional.of(auditorInfo);
        }
        // Caso o principal não seja do tipo Perfil, retorna o valor padrão.
        return Optional.of(authentication.getName());
    }
}