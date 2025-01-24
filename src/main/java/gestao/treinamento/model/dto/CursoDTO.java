package gestao.treinamento.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoDTO {

    private Long id;
    private String nome;
    private String conteudoProgramatico;
    private Integer cargaHorariaTotal;
    private Integer cargaHorariaEad;
    private Integer cargaHorariaPresencial;
    private Integer periodoValidade;
    private Double valorContratoCrm;

    private List<Long> idEmpresaVinculo;
    private List<String> nomeEmpresaVinculo;
}