//package gestao.treinamento.service.consultas;
//
//import gestao.treinamento.model.dto.consultas.NivelVisibilidadeConsultaDTO;
//import gestao.treinamento.repository.consultas.ConsultaNiveisVisibilidadeRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@AllArgsConstructor
//@Service
//public class ConsultaNiveisVisibilidadeService {
//
//    @Autowired
//    private final ConsultaNiveisVisibilidadeRepository repository;
//
//    public List<NivelVisibilidadeConsultaDTO> consultaCadastro() {
//        return repository.findAllNiveisVisibilidadesDTO();
//    }
//}