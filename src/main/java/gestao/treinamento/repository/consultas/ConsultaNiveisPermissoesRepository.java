package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.NivelPermissaoConsultaDTO;
import gestao.treinamento.model.entidades.NivelPermissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaNiveisPermissoesRepository extends JpaRepository<NivelPermissao, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.NivelPermissaoConsultaDTO(np.id, np.nome) FROM NivelPermissao np")
    List<NivelPermissaoConsultaDTO> findAllNiveisPermissoesDTO();
}
