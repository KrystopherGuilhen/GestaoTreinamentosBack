package gestao.treinamento.service.consultas;

import gestao.treinamento.model.entidade.Instrutor;
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

    public List<Instrutor> consultaCadastro() {
        return repository.findAll();
    }
}
