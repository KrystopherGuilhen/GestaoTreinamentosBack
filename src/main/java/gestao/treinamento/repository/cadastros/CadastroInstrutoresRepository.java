package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroInstrutoresRepository extends JpaRepository<Instrutor, Long> {

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Instrutor i WHERE i.cpf = :value OR i.cnpj = :value")
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

    boolean existsByTelefone(String telefone);

    // Novos métodos para validação na atualização
    boolean existsByCpfAndIdNot(String cpf, Long id);

    boolean existsByCnpjAndIdNot(String cnpj, Long id);

    boolean existsByTelefoneAndIdNot(String telefone, Long id);
}