package gestao.treinamento.model.dto.cadastros;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PalestraDTO {

    private Long id;
    private String nomeEvento;
//    private Long idCidade;
//    private String NomeCidade;
    //private String dataInicio;
    //private String dataFim;
    //private Double valorContratoCrm;
    //private Integer numeroContratoCrm;
    private String conteudoProgramatico;
    private String observacao;
    private Integer cargaHorariaTotal;

//    private List<Long> idEmpresaVinculo;
//    private List<String> nomeEmpresaVinculo;
}
