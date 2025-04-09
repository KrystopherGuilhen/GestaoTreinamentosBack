package gestao.treinamento.model.dto.consultas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CidadeConsultaDTO {

    private Long id;
    private Long estadoId;
    private Long ibgeId;
    private String nome;
}
