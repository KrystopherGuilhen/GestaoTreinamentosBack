package gestao.treinamento.controller;

import gestao.treinamento.model.dto.login.LoginDTO;
import gestao.treinamento.model.entidades.Perfil;
import gestao.treinamento.service.login.LoginPerfilService;
import gestao.treinamento.service.login.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin
public class LoginController {

    private final LoginPerfilService perfilService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            UserDetails userDetails = perfilService.loadUserByUsername(loginDTO.getIdentificador());

            if (!passwordEncoder.matches(loginDTO.getSenha(), userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensagem", "Credenciais inválidas"));
            }

            Perfil perfil = (Perfil) userDetails;
            String token = tokenService.gerarToken(userDetails);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "usuario", Map.of(
                            "id", perfil.getId(),
                            "nome", perfil.getNome(),
                            "email", perfil.getEmail()
                    )
            ));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Credenciais inválidas"));
        }
    }
}