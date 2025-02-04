package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.TurmaConsultaDTO;
import gestao.treinamento.model.entidades.Turma;
import gestao.treinamento.repository.consultas.ConsultaTurmasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaTurmasService {

    @Autowired
    private final ConsultaTurmasRepository repository;

    public List<TurmaConsultaDTO> consultaCadastro() {
        return repository.findAllTurmasDTO();
    }
}
