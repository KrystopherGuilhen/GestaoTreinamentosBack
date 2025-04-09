package gestao.treinamento.model.dto.cadastros;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDTO {

    private Long id;
    private String nome;
    private Long idEstado;
    private String nomeEstado;
    private Long idCidade;
    private String NomeCidade;
    private String telefone;
    private String cnpj;
    private String cpf;
    private String nomeResponsavelEmpresa;
    private String emailResponsavelEmpresa;
    private String relacaoEspacoConfinado;

    private Long idIndustriaVinculo;
    private List<String> nomeIndustriaVinculo;
}
