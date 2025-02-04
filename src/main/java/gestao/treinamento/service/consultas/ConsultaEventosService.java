package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.EventoConsultaDTO;
import gestao.treinamento.model.entidades.Evento;
import gestao.treinamento.repository.consultas.ConsultaEventosRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaEventosService {

    @Autowired
    private final ConsultaEventosRepository repository;

    public List<EventoConsultaDTO> consultaCadastro() {
        return repository.findAllEventosDTO();
    }
}
