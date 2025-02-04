package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.CursoConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaCursosRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaCursosService {

    @Autowired
    private final ConsultaCursosRepository repository;

    public List<CursoConsultaDTO> consultaCadastro() {
        return repository.findAllCursosDTO();
    }
}
