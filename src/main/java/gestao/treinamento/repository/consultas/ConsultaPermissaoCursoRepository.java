package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoCursoConsultaDTO;
import gestao.treinamento.model.entidades.PermissaoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaPermissaoCursoRepository extends JpaRepository<PermissaoCurso, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.PermissaoCursoConsultaDTO(pc.id, pc.nome) FROM PermissaoCurso pc")
    List<PermissaoCursoConsultaDTO> findAllPermissaoCursoDTO();
}
