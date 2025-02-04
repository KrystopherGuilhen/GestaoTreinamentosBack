package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.InstrutorConsultaDTO;
import gestao.treinamento.model.entidades.Instrutor;
import gestao.treinamento.repository.consultas.ConsultaInstrutoresRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaInstrutoresService {

    @Autowired
    private final ConsultaInstrutoresRepository repository;

    public List<InstrutorConsultaDTO> consultaCadastro() {
        return repository.findAllInstrutoresDTO();
    }
}
