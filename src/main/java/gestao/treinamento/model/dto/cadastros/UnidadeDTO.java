package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeDTO {

    private Long id;
    private String nome;

    private Long idResponsavelTecnicoVinculo;
    private List<String> nomeResponsavelTecnicoVinculo;

    private Long idPerfilVinculo;
    private List<String> nomePerfilVinculo;

}
