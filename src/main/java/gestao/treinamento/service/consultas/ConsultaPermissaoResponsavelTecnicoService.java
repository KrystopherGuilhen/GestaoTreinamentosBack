package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoResponsavelTecnicoConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoResponsavelTecnicoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoResponsavelTecnicoService {

    @Autowired
    private final ConsultaPermissaoResponsavelTecnicoRepository repository;

    public List<PermissaoResponsavelTecnicoConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoResponsavelTecnicoDTO();
    }
}