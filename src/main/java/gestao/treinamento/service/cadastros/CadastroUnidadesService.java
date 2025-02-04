package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.entidades.Unidade;
import gestao.treinamento.repository.cadastros.CadastroUnidadesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CadastroUnidadesService {
    @Autowired
    private final CadastroUnidadesRepository repository;

    public List<Unidade> consultaCadastro() {
        return repository.findAll();
    }

    public Unidade salvarUnidade(Unidade unidade) {
        return repository.save(unidade);
    }

    public Unidade atualizarUnidade(Long id, Unidade unidade) {
        Unidade existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrado com ID: " + id));

        existente.setNomeUnidade(unidade.getNomeUnidade());
        existente.setGerenteResponsavel(unidade.getGerenteResponsavel());
        existente.setResponsavelTecnico(unidade.getResponsavelTecnico());

        return repository.save(existente);
    }

    public void deletarUnidade(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Unidade não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    public void deletarUnidades(List<Long> ids) {
        List<Unidade> unidades = repository.findAllById(ids);
        if (unidades.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
        }
        repository.deleteAll(unidades);
    }
}
