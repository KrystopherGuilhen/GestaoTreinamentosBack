package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsavelTecnicoDTO {

    private Long id;
    private String nome;
    private String cpf;
    private Integer numeroConselho;

}
