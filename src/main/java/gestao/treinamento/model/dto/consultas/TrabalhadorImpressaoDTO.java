package gestao.treinamento.model.dto.consultas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrabalhadorImpressaoDTO {
    private Long idTrabalhador;
    private String nome;
    private String cidade;
    private String cpf;
    private String rg;
    //private String numeroRegistro;
}