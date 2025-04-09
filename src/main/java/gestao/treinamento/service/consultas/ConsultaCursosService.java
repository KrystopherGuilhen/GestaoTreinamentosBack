package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.CursoConsultaDTO;
import gestao.treinamento.model.dto.consultas.RelatorioCursoConsultaDTO;
import gestao.treinamento.repository.consultas.ConsultaCursosRepository;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ConsultaCursosService {

    @Autowired
    private final ConsultaCursosRepository repository;

    public List<CursoConsultaDTO> consultaCadastro() {
        return repository.findAllCursosDTO();
    }

    public List<RelatorioCursoConsultaDTO> relatorioCursosFinalizados() {
        List<Tuple> tuplas = repository.findRelatorioCursos();
        List<RelatorioCursoConsultaDTO> dtos = tuplas.stream().map(tuple -> new RelatorioCursoConsultaDTO(
                tuple.get("idCurso", Long.class),
                tuple.get("nomeCurso", String.class),
                tuple.get("conteudoProgramatico", String.class),
                tuple.get("periodoValidadeCurso", String.class),
                tuple.get("cargaHorariaTotal", Integer.class),
                tuple.get("idTurma", Long.class),
                tuple.get("nomeTurma", String.class),
                tuple.get("dataInicio", String.class),
                tuple.get("dataFim", String.class),
                tuple.get("nomeCidadeTreinamento", String.class),
                tuple.get("numeroContrato", String.class),
                tuple.get("valorContratoCrm", Double.class),
                tuple.get("idModalidade", Long.class),
                tuple.get("nomeModalidade", String.class)
        )).collect(Collectors.toList());

        return dtos;
    }
}