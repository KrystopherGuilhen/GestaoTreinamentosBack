package gestao.treinamento.service.consultas;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.consultas.TrabalhadorImpressaoDTO;
import gestao.treinamento.model.dto.consultas.TurmaImpressaoDTO;
import gestao.treinamento.model.entidades.Turma;
import gestao.treinamento.repository.consultas.ConsultaTurmasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Service
public class ConsultaTurmasService {

    @Autowired
    private final ConsultaTurmasRepository repository;

    public TurmaImpressaoDTO consultaParaImpressao(Long idTurma, List<Long> idTrabalhadores) {
        Turma turma = repository.findById(idTurma)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada"));

        return convertToImpressaoDTO(turma, idTrabalhadores);
    }

    private TurmaImpressaoDTO convertToImpressaoDTO(Turma turma, List<Long> idTrabalhadores) {
        TurmaImpressaoDTO dto = new TurmaImpressaoDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Dados básicos da turma
        dto.setIdTurma(turma.getId());
        dto.setNomeTurma(turma.getNome());
        dto.setDataInicio(turma.getDataInicio().format(formatter));
        dto.setDataFim(turma.getDataFim().format(formatter));

        // Dados do curso
        turma.getTurmaCursosVinculados().stream().findFirst().ifPresent(tc -> {
            dto.setNomeCurso(tc.getCurso().getNome());
            dto.setCargaHoraria(tc.getCurso().getCargaHorariaTotal());
            dto.setConteudoProgramaticoCurso(tc.getCurso().getConteudoProgramatico());
        });

        // Dados da modalidade
        turma.getTurmaModalidadesVinculadas().stream().findFirst().ifPresent(tm -> {
            dto.setModalidade(tm.getModalidade().getNome());
        });

        // Dados da unidade
        turma.getTurmaUnidadesVinculadas().stream().findFirst().ifPresent(tu -> {
            dto.setUnidadePolo(tu.getUnidade().getNome());
            dto.setNomeGerenteResponsavel(tu.getUnidade().getGerenteResponsavel());
        });

        // Dados do instrutor
        turma.getTurmaInstrutoresVinculados().stream().findFirst().ifPresent(ti -> {
            dto.setNomeInstrutor(ti.getInstrutor().getNome());
        });

        // Filtra trabalhadores selecionados
        List<TrabalhadorImpressaoDTO> trabalhadores = turma.getTurmaTrabalhadoresVinculados().stream()
                .filter(tt -> idTrabalhadores.contains(tt.getTrabalhador().getId()))
                .map(tt -> new TrabalhadorImpressaoDTO(
                        tt.getTrabalhador().getId(),
                        tt.getTrabalhador().getNome(),
                        tt.getTrabalhador().getCidade()
                ))
                .toList();

        dto.setTrabalhadores(trabalhadores);

        return dto;
    }
}
