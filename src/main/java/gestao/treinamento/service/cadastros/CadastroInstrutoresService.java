package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.entidades.Instrutor;
import gestao.treinamento.repository.cadastros.CadastroInstrutoresRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CadastroInstrutoresService {

    @Autowired
    private final CadastroInstrutoresRepository repository;

    public List<Instrutor> consultaCadastro() {
        return repository.findAll();
    }

    public Instrutor salvarInstrutor(Instrutor instrutor) {
        return repository.save(instrutor);
    }

    public Instrutor atualizarInstrutor(Long id, Instrutor instrutor) {
        Instrutor existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + id));

        existente.setNome(instrutor.getNome());
        existente.setCidade(instrutor.getCidade());
        existente.setEstado(instrutor.getEstado());
        existente.setTelefone(instrutor.getTelefone());
        existente.setCpf(instrutor.getCpf());
        existente.setEmail(instrutor.getEmail());
        existente.setFormacao(instrutor.getFormacao());
        existente.setNumeroRegistroProfissional(instrutor.getNumeroRegistroProfissional());
        existente.setUnidadeRegistroProfissional(instrutor.getUnidadeRegistroProfissional());
        existente.setEstadoRegistroProfissional(instrutor.getEstadoRegistroProfissional());

        return repository.save(existente);
    }

    public void deletarInstrutor(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Instrutor não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    public void deletarInstrutores(List<Long> ids) {
        List<Instrutor> instrutores = repository.findAllById(ids);
        if (instrutores.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
        }
        repository.deleteAll(instrutores);
    }
}