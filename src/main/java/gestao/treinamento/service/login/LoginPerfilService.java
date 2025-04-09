package gestao.treinamento.service.login;

import gestao.treinamento.model.entidades.Perfil;
import gestao.treinamento.repository.login.LoginPerfilRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginPerfilService {

    private final LoginPerfilRepository perfilRepository;

    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {
        // System.out.println("[DEBUG] Buscando usuário por identificador: " + identificador);
        Optional<Perfil> perfil = perfilRepository.findByEmailOrNome(identificador);
        if (perfil.isEmpty()) {
            //    System.out.println("[DEBUG] Usuário não encontrado: " + identificador);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        //    System.out.println("[DEBUG] Usuário encontrado: " + perfil.get().getEmail());
        return perfil.get();
    }
}
