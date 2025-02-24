package gestao.treinamento.model.dto.consultas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrutorFormacaoConsultaListaDTO {

    private Long formacaoId;       // ID da formação
    private String formacaoNome;   // Nome da formação
}
