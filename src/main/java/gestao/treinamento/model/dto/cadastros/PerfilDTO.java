package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {

    private Long id;
    private String nome;

//    private Long idUnidadeVinculo;
//    private List<String> nomeUnidadeVinculo;

    private List<Long> idNivelPermissaoVinculo;
    private List<String> nomeNivelPermissaoVinculo;

}
