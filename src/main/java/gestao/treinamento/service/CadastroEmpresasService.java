package gestao.treinamento.service;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.entidade.Empresa;
import gestao.treinamento.repository.CadastroEmpresasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + id));

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
            throw new ResourceNotFoundException("Instrutor não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    public void deletarEmpresas(List<Long> ids) {
        List<Empresa> empresas = repository.findAllById(ids);
        if (empresas.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
        }
        repository.deleteAll(empresas);
    }
}