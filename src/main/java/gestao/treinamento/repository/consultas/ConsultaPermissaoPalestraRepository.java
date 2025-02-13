package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoPalestraConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoPalestra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoPalestraRepository extends JpaRepository<PermissaoPalestra, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoPalestraConsultaDTO(pc.id, pc.nome) FROM PermissaoPalestra pc")
    List<PermissaoPalestraConsultaDTO> findAllPermissaoPalestraDTO();
}
