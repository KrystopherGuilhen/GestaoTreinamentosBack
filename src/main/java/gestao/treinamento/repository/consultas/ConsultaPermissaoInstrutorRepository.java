package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoInstrutorConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoInstrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoInstrutorRepository extends JpaRepository<PermissaoInstrutor, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoInstrutorConsultaDTO(pi.id, pi.nome) FROM PermissaoInstrutor pi")
    List<PermissaoInstrutorConsultaDTO> findAllPermissaoInstrutorDTO();
}
