package gestao.treinamento.model.dto.consultas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoConsultaDTO {

    private Long id;
    private Long ibgeId;
    private Long ibgeRegiaoId;
    private String nome;
    private String sigla;
}