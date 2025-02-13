package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoTrabalhadorConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoTrabalhadorRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoTrabalhadorService {

    @Autowired
    private final ConsultaPermissaoTrabalhadorRepository repository;

    public List<PermissaoTrabalhadorConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoTrabalhadorDTO();
    }
}