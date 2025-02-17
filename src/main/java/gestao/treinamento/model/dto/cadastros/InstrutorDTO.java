package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrutorDTO {

    private Long id;
    private String nome;
    private String cidade;
    private String estado;
    private String telefone;
    private String cpf;
    private String cnpj;
    private String email;
    private byte[] certificado;
    private String formacao;
    private String experiencia;
    private Integer numeroRegistroProfissional;
    private String unidadeRegistroProfissional;
    private String estadoRegistroProfissional;

    private Long idTipoPessoaVinculado;
    private String nomeTipoPessoaVinculado;
}
