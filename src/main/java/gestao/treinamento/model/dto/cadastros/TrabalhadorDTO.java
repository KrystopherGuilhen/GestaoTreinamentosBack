package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrabalhadorDTO {

    private Long id;
    private String nome;
    private String cidade;
    private String estado;
    private String telefone;
    private String cpf;
    private String rg;
    private String dataNascimento;
    private String email;

    private List<Long> idEmpresaVinculo;
    private List<String> nomeEmpresaVinculo;

}