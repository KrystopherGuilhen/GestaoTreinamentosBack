package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.UnidadeConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaUnidadesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaUnidadesService {

    @Autowired
    private final ConsultaUnidadesRepository repository;

    public List<UnidadeConsultaDTO> consultaCadastro() {
        return repository.findAllUnidadesDTO();
    }
}
