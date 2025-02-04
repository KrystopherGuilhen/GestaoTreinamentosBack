package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.EmpresaConsultaDTO;
import gestao.treinamento.model.entidades.Empresa;
import gestao.treinamento.repository.consultas.ConsultaEmpresasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaEmpresasService {

    @Autowired
    private final ConsultaEmpresasRepository repository;

    public List<EmpresaConsultaDTO> consultaCadastro() {
        return repository.findAllEmpresasDTO();
    }
}
