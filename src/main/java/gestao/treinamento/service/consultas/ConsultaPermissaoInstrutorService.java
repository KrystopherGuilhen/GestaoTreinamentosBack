package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoInstrutorConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoInstrutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoInstrutorService {

    @Autowired
    private final ConsultaPermissaoInstrutorRepository repository;

    public List<PermissaoInstrutorConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoInstrutorDTO();
    }
}