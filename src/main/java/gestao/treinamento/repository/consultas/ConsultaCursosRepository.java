package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.CursoConsultaDTO;
import gestao.treinamento.model.entidades.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaCursosRepository extends JpaRepository<Curso, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.CursoConsultaDTO(c.id, c.nome) FROM Curso c")
    List<CursoConsultaDTO> findAllCursosDTO();
}
