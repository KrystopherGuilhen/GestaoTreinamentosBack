package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoEmpresaConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoEmpresaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoEmpresaService {

    @Autowired
    private final ConsultaPermissaoEmpresaRepository repository;

    public List<PermissaoEmpresaConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoEmpresaDTO();
    }
}