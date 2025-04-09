package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Cidades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadastroCidadesRepository extends JpaRepository<Cidades, Long> {
    Optional<Cidades> findByNomeIgnoreCaseAndEstadoId(String nome, Long estadoId);
}
