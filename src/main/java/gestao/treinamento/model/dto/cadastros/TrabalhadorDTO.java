package gestao.treinamento.model.dto.cadastros;

import jakarta.persistence.Column;
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
    private Long idEstado;
    private String nomeEstado;
    private Long idCidade;
    private String NomeCidade;
    private String telefone;
    private String cpf;
    private String rg;
    private String dataNascimento;
    private String email;

    private List<Long> idEmpresaVinculo;
    private List<String> nomeEmpresaVinculo;

}