package gestao.treinamento.model.dto.consultas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrutorFormacaoConsultaDTO {

    private Long id;
    private String nome;
}
