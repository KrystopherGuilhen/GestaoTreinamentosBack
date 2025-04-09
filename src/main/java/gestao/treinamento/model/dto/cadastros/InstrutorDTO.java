package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrutorDTO {

    private Long id;
    private String nome;
    private Long idEstado;
    private String nomeEstado;
    private Long idCidade;
    private String NomeCidade;
    private String telefone;
    private String cpf;
    private String cnpj;
    private String email;
    private List<InstrutorCertificadosDTO> certificados;
    private boolean possuiMultiplasFormacoes;
    private List<String> formacoes;
    private String experiencia;
    private Integer numeroRegistroProfissional;
    private String unidadeRegistroProfissional;
    private Long idEstadoRegistroProfissional;
    private String nomeEstadoRegistroProfissional;

    private Long idTipoPessoaVinculado;
    private String nomeTipoPessoaVinculado;

    //crie o modelo de importação unica e ajuste aqui para o byte[] para poder salvar diretamente na tabela de instrutor.
    private List<InstrutorAssinaturaDTO> assinatura;
}
