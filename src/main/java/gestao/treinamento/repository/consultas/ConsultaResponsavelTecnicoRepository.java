package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.ResponsavelTecnicoConsultaDTO;
import gestao.treinamento.model.entidades.ResponsavelTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaResponsavelTecnicoRepository extends JpaRepository<ResponsavelTecnico, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.ResponsavelTecnicoConsultaDTO(rt.id, rt.nome, rt.cpf) FROM ResponsavelTecnico rt")
    List<ResponsavelTecnicoConsultaDTO> findAllResponsavelTecnicoDTO();
}
