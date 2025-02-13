package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoTurmaConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoTurma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoTurmaRepository extends JpaRepository<PermissaoTurma, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoTurmaConsultaDTO(pc.id, pc.nome) FROM PermissaoTurma pc")
    List<PermissaoTurmaConsultaDTO> findAllPermissaoTurmaDTO();
}
