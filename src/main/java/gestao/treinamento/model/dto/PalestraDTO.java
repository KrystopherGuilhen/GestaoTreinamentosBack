package gestao.treinamento.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PalestraDTO {

    private Long id;
    private String nomeEvento;
    private String cidadeEvento;
    private String dataInicio;
    private String dataFim;
    private Double valorContratoCrm;
    private Integer numeroContratoCrm;

    private List<Long> idEmpresaVinculo;
    private List<String> nomeEmpresaVinculo;
}
