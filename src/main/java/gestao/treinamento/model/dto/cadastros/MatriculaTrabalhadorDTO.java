package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatriculaTrabalhadorDTO {

    private Long id;

    private Long idTrabalhadorVinculo;
    private List<String> nomeTrabalhadorVinculo;

    private Long idTurmaVinculo;
    private List<String> nomeTurmaVinculo;

    private Long idCursoVinculo;
    private List<String> nomeCursoVinculo;

    private Integer cargaHorariaTotal;
}
