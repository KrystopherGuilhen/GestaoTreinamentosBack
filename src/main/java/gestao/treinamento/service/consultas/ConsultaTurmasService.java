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
import java.util.Base64;
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
        dto.setNomeCidadeTreinamento(turma.getNomeCidadeTreinamento());
        dto.setObservacaoNr(turma.getObservacaoNr());

        // Dados do empresa
        turma.getTurmaEmpresasVinculadas().stream().findFirst().ifPresent(tc -> {
            dto.setRelacaoEspacoConfinado(tc.getEmpresa().getRelacaoEspacoConfinado());
        });

        // Dados do curso
        turma.getTurmaCursosVinculados().stream().findFirst().ifPresent(tc -> {
            dto.setNomeCurso(tc.getCurso().getNome());
            dto.setCargaHoraria(tc.getCurso().getCargaHorariaTotal());
            dto.setConteudoProgramaticoCurso(tc.getCurso().getConteudoProgramatico());
            dto.setPeriodoValidadeCurso(tc.getCurso().getPeriodoValidade());
        });

        // Dados da Palestra
        turma.getTurmaPalestrasVinculadas().stream().findFirst().ifPresent(tc -> {
            dto.setNomePalestra(tc.getPalestra().getNomeEvento());
            dto.setCargaHorariaPalestra(tc.getPalestra().getCargaHorariaTotal());
            dto.setConteudoProgramaticoPalestra(tc.getPalestra().getConteudoProgramatico());
        });

        // Dados da modalidade
        turma.getTurmaModalidadesVinculadas().stream().findFirst().ifPresent(tm -> {
            dto.setModalidade(tm.getModalidade().getNome());
        });

        // Dados de evento
        turma.getTurmaEventosVinculados().stream().findFirst().ifPresent(tm -> {
            dto.setIdEvento(tm.getEvento().getId());
            dto.setNomeEvento(tm.getEvento().getNome());
        });

        // Dados da unidade
        turma.getTurmaUnidadesVinculadas().stream().findFirst().ifPresent(tu -> {
            dto.setUnidadePolo(tu.getUnidade().getNome());
            dto.setNomeGerenteResponsavel(tu.getUnidade().getGerenteResponsavel());
            dto.setRazaoSocialUnidade(tu.getUnidade().getRazaoSocial());
            dto.setNomeFantasiaUnidade(tu.getUnidade().getNomeFantasia());
            dto.setCnpjUnidade(tu.getUnidade().getCnpj());
            dto.setEnderecoUnidade(tu.getUnidade().getEndereco());
            dto.setCredenciamentoUnidade(tu.getUnidade().getCredenciamento());
            String mimeType = tu.getUnidade().getAssinatura().get(0).getMimeType();
            String base64 = Base64.getEncoder().encodeToString(tu.getUnidade().getAssinatura().get(0).getDados());
            dto.setAssinaturaGerenteResponsavel("data:" + mimeType + ";base64," + base64);
            dto.setNomeResponsavelTecnico(tu.getUnidade().getUnidadeResponsavelTecnicosVinculados().get(0).getResponsavelTecnico().getNome());
            String mimeTypeResponsavel = tu.getUnidade().getUnidadeResponsavelTecnicosVinculados().get(0).getResponsavelTecnico().getAssinatura().get(0).getMimeType();
            String base64Resposavel = Base64.getEncoder().encodeToString(tu.getUnidade().getUnidadeResponsavelTecnicosVinculados().get(0).getResponsavelTecnico().getAssinatura().get(0).getDados());
            dto.setAssinaturaResponsavelTecnico("data:" + mimeTypeResponsavel + ";base64," + base64Resposavel);
            dto.setNumeroConselhoMT(tu.getUnidade().getUnidadeResponsavelTecnicosVinculados().get(0).getResponsavelTecnico().getNumeroConselho());
        });

        // Adaptando a coleta dos instrutores para suportar múltiplos vínculos (Instrutor, InstrutorUm e InstrutorDois)
        if (turma.getTurmaInstrutoresVinculados() != null && !turma.getTurmaInstrutoresVinculados().isEmpty()) {
            // Inicializa os campos para evitar dados residuais
            dto.setNomeInstrutor(null);
            dto.setFormacaoInstrutor(null);
            dto.setAssinaturaInstrutor(null);
            dto.setNomeInstrutorUm(null);
            dto.setNomeInstrutorDois(null);

            // Itera sobre os vínculos e popula de acordo com o idMultiploInstrutor
            for (var ti : turma.getTurmaInstrutoresVinculados()) {
                if (ti.getIdMultiploInstrutor() == 1) {
                    dto.setNomeInstrutor(ti.getInstrutor().getNome());
                    // Verifica se há formação e assinatura disponíveis para o Instrutor principal
                    if (ti.getInstrutor().getFormacoes() != null && !ti.getInstrutor().getFormacoes().isEmpty()) {
                        dto.setFormacaoInstrutor(ti.getInstrutor().getFormacoes().get(0).getFormacao());
                    }
                    if (ti.getInstrutor().getAssinatura() != null && !ti.getInstrutor().getAssinatura().isEmpty()) {
                        String mimeType = ti.getInstrutor().getAssinatura().get(0).getMimeType();
                        String base64 = Base64.getEncoder().encodeToString(ti.getInstrutor().getAssinatura().get(0).getDados());
                        dto.setAssinaturaInstrutor("data:" + mimeType + ";base64," + base64);
                    }
                } else if (ti.getIdMultiploInstrutor() == 2) {
                    dto.setNomeInstrutorUm(ti.getInstrutor().getNome());
                    if (ti.getInstrutor().getFormacoes() != null && !ti.getInstrutor().getFormacoes().isEmpty()) {
                        dto.setFormacaoInstrutorUm(ti.getInstrutor().getFormacoes().get(0).getFormacao());
                    }
                } else if (ti.getIdMultiploInstrutor() == 3) {
                    dto.setNomeInstrutorDois(ti.getInstrutor().getNome());
                    if (ti.getInstrutor().getFormacoes() != null && !ti.getInstrutor().getFormacoes().isEmpty()) {
                        dto.setFormacaoInstrutorDois(ti.getInstrutor().getFormacoes().get(0).getFormacao());
                    }
                }
            }
        }

        // Filtra trabalhadores selecionados
        List<TrabalhadorImpressaoDTO> trabalhadores = turma.getTurmaTrabalhadoresVinculados().stream()
                .filter(tt -> idTrabalhadores.contains(tt.getTrabalhador().getId()))
                .map(tt -> new TrabalhadorImpressaoDTO(
                        tt.getTrabalhador().getId(),
                        tt.getTrabalhador().getNome(),
                        tt.getTrabalhador().getNomeCidade(),
                        tt.getTrabalhador().getCpf(),
                        tt.getTrabalhador().getRg()
                ))
                .toList();

        dto.setTrabalhadores(trabalhadores);

        return dto;
    }
}
