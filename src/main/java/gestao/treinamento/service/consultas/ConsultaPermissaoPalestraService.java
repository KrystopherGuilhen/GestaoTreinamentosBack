package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoPalestraConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoPalestraRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoPalestraService {

    @Autowired
    private final ConsultaPermissaoPalestraRepository repository;

    public List<PermissaoPalestraConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoPalestraDTO();
    }
}