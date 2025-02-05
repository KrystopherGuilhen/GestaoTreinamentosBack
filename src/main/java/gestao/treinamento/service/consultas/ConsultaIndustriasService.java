package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.EventoConsultaDTO;
import gestao.treinamento.model.dto.consultas.IndustriaConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaEventosRepository;
import gestao.treinamento.repository.consultas.ConsultaIndustriasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaIndustriasService {

    @Autowired
    private final ConsultaIndustriasRepository repository;

    public List<IndustriaConsultaDTO> consultaCadastro() {
        return repository.findAllIndustriasDTO();
    }
}
