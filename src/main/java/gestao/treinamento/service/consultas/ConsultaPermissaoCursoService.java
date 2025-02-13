package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoCursoConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoCursoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoCursoService {

    @Autowired
    private final ConsultaPermissaoCursoRepository repository;

    public List<PermissaoCursoConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoCursoDTO();
    }
}