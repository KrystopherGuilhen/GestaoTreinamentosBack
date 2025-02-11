package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilNivelPermissaoDTO {

    private Long idPermissao;
    private String nomePermissao;

    List<PerfilNivelVisibilidadeDTO> perfilNivelVisibilidadeDTO;
}
