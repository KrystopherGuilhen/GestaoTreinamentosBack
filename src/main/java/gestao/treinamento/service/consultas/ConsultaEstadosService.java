package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.EstadoConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaEstadosRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaEstadosService {

    @Autowired
    private final ConsultaEstadosRepository repository;

    public List<EstadoConsultaDTO> consultaCadastro() {
        return repository.findAllEstadosDTO();
    }
}
