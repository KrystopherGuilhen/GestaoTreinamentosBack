package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoUnidadeConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoUnidadeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoUnidadeService {

    @Autowired
    private final ConsultaPermissaoUnidadeRepository repository;

    public List<PermissaoUnidadeConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoUnidadeDTO();
    }
}