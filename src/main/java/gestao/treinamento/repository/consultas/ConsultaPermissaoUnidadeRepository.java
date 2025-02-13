package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoUnidadeConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoUnidadeRepository extends JpaRepository<PermissaoUnidade, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoUnidadeConsultaDTO(pc.id, pc.nome) FROM PermissaoUnidade pc")
    List<PermissaoUnidadeConsultaDTO> findAllPermissaoUnidadeDTO();
}
