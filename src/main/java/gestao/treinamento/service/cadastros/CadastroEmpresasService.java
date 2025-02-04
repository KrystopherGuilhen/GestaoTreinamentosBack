package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.entidades.Empresa;
import gestao.treinamento.repository.cadastros.CadastroEmpresasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CadastroEmpresasService {

    @Autowired
    private final CadastroEmpresasRepository repository;

    public List<Empresa> consultaCadastro() {
        return repository.findAll();
    }

    public Empresa salvarEmpresa(Empresa instrutor) {
        return repository.save(instrutor);
    }

    public Empresa atualizarEmpresa(Long id, Empresa instrutor) {
        Empresa existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrado com ID: " + id));

        existente.setNome(instrutor.getNome());
        existente.setCidade(instrutor.getCidade());
        existente.setEstado(instrutor.getEstado());
        existente.setTelefone(instrutor.getTelefone());
        existente.setCnpj(instrutor.getCnpj());
        existente.setNomeResponsavelEmpresa(instrutor.getNomeResponsavelEmpresa());
        existente.setEmailResponsavelEmpresa(instrutor.getEmailResponsavelEmpresa());

        return repository.save(existente);
    }

    public void deletarEmpresa(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa não encontrada com ID: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("A empresa não pode ser excluída pois está vinculada a outro cadastro.");
        }
    }

    public void deletarEmpresas(List<Long> ids) {
        List<Empresa> empresas = repository.findAllById(ids);
        if (empresas.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
        }
        try {
            repository.deleteAll(empresas);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Uma ou mais empresas não podem ser excluídas pois estão vinculadas a outros cadastros.");
        }
    }
}