package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.entidade.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaEventosRepository extends JpaRepository<Evento, Long> {
}
