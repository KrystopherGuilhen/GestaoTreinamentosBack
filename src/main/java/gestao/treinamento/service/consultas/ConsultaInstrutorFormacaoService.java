package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.InstrutorFormacaoConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaInstrutorFormacaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaInstrutorFormacaoService {


    @Autowired
    private ConsultaInstrutorFormacaoRepository repository;

    public List<InstrutorFormacaoConsultaDTO> consultaPorInstrutores(List<Long> instrutorId) {
        return repository.findAllFormacoesByInstrutorIds(instrutorId);
    }
}