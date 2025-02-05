package gestao.treinamento.model.dto.cadastros;

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
    private String cidade;
    private String estado;
    private String telefone;
    private String cnpj;
    private String cpf;
    private String nomeResponsavelEmpresa;
    private String emailResponsavelEmpresa;
    private String relacaoEspacoConfinado;

    private Long idIndustriaVinculo;
    private List<String> nomeIndustriaVinculo;
}
