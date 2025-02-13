package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoTurmaConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoTurmaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoTurmaService {

    @Autowired
    private final ConsultaPermissaoTurmaRepository repository;

    public List<PermissaoTurmaConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoTurmaDTO();
    }
}