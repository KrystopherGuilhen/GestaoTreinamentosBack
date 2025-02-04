package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroEventosRepository extends JpaRepository<Evento, Long> {
}
