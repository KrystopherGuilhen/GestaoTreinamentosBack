package gestao.treinamento.service.consultas;

import gestao.treinamento.model.entidade.Trabalhador;
import gestao.treinamento.repository.consultas.ConsultaTrabalhadoresRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaTrabalhadoresService {

    @Autowired
    private final ConsultaTrabalhadoresRepository repository;

    public List<Trabalhador> consultaCadastro() {
        return repository.findAll();
    }
}
