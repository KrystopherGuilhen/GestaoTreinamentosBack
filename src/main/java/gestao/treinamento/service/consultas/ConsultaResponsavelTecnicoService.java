package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.ResponsavelTecnicoConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaResponsavelTecnicoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaResponsavelTecnicoService {

    @Autowired
    private final ConsultaResponsavelTecnicoRepository repository;

    public List<ResponsavelTecnicoConsultaDTO> consultaCadastro() {
        return repository.findAllResponsavelTecnicoDTO();
    }
}