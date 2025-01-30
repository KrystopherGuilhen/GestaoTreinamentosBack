package gestao.treinamento.service.consultas;

import gestao.treinamento.model.entidade.Modalidade;
import gestao.treinamento.repository.consultas.ConsultaModalidadesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaModalidadesService {

    @Autowired
    private final ConsultaModalidadesRepository repository;

    public List<Modalidade> consultaCadastro() {
        return repository.findAll();
    }
}
