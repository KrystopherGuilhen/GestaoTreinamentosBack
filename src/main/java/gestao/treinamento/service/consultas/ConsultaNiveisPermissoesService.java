package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.NivelPermissaoConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaNiveisPermissoesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaNiveisPermissoesService {

    @Autowired
    private final ConsultaNiveisPermissoesRepository repository;

    public List<NivelPermissaoConsultaDTO> consultaCadastro() {
        return repository.findAllNiveisPermissoesDTO();
    }
}