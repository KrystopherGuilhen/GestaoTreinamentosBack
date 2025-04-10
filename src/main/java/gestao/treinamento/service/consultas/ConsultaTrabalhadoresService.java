package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.TrabalhadorConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaTrabalhadoresRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaTrabalhadoresService {

    @Autowired
    private final ConsultaTrabalhadoresRepository repository;

    public List<TrabalhadorConsultaDTO> consultaCadastro() {
        return repository.findAllTrabalhadoresDTO();
    }

    public List<TrabalhadorConsultaDTO> consultaPorEmpresas(List<Long> empresaIds) {
        return repository.findAllTrabalhadoresByEmpresaIds(empresaIds);
    }

    public List<TrabalhadorConsultaDTO> consultaPorTurma(Long turmaId) {
        return repository.findAllTrabalhadoresByTurmaId(turmaId);
    }
}
