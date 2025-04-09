package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PalestraConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPalestrasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPalestrasService {

    @Autowired
    private final ConsultaPalestrasRepository repository;

    public List<PalestraConsultaDTO> consultaCadastro() {
        return repository.findAllPalestrasDTO();
    }
}
