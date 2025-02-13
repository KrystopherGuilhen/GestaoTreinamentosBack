package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.PermissaoPerfilConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaPermissaoPerfilRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaPermissaoPerfilService {

    @Autowired
    private final ConsultaPermissaoPerfilRepository repository;

    public List<PermissaoPerfilConsultaDTO> consultaCadastro() {
        return repository.findAllPermissaoPerfilDTO();
    }
}