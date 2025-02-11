package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilNivelVisibilidadeDTO {

    private Long idVisibilidade;
    private String nomeVisibilidade;
}
