package gestao.treinamento.repository.login;

import gestao.treinamento.model.entidades.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoginPerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByEmail(String email);

    @Query("SELECT p FROM Perfil p WHERE p.email = :identificador OR p.nome = :identificador")
    Optional<Perfil> findByEmailOrNome(@Param("identificador") String identificador);
}
