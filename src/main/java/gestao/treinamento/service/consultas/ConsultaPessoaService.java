package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PessoaConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPessoaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPessoaService {

    @Autowired
    private final ConsultaPessoaRepository repository;

    public List<PessoaConsultaDTO> consultaCadastro() {
        return repository.findAllPessoasDTO();
    }
}
