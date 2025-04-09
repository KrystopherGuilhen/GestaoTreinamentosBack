package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.CidadeConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaCidadesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaCidadesService {

    @Autowired
    private final ConsultaCidadesRepository repository;


    public List<CidadeConsultaDTO> consultaCadastro(Long idEstado) {
        return repository.findByIdEstado(idEstado);
    }
}
