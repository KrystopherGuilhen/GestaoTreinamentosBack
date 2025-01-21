package gestao.treinamento.service;

import gestao.treinamento.model.entidade.Empresa;
import gestao.treinamento.repository.ConsultaEmpresasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaEmpresasService {

    @Autowired
    private final ConsultaEmpresasRepository repository;

    public List<Empresa> consultaCadastro() {
        return repository.findAll();
    }
}
