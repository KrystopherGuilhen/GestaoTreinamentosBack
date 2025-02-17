package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroEmpresasRepository extends JpaRepository<Empresa, Long> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Empresa e WHERE e.cpf = :value OR e.cnpj = :value")
    boolean existsByCpfOrCnpj(@Param("value") String value);

    // Ou para campos específicos
    default boolean existsByField(String fieldName, String value) {
        return switch (fieldName.toLowerCase()) {
            case "cpf" -> existsByCpf(value);
            case "cnpj" -> existsByCnpj(value);
            default -> throw new IllegalArgumentException("Campo inválido: " + fieldName);
        };
    }

    boolean existsByCpf(String cpf);

    boolean existsByCnpj(String cnpj);
}
