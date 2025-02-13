//package gestao.treinamento.repository.consultas;
//
//import gestao.treinamento.model.dto.consultas.NivelVisibilidadeConsultaDTO;
//import gestao.treinamento.model.entidades.NivelVisibilidade;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ConsultaNiveisVisibilidadeRepository extends JpaRepository<NivelVisibilidade, Long> {
//
//    @Query("SELECT new gestao.treinamento.model.dto.consultas.NivelVisibilidadeConsultaDTO(nv.id, nv.nome) FROM NivelVisibilidade nv")
//    List<NivelVisibilidadeConsultaDTO> findAllNiveisVisibilidadesDTO();
//}
