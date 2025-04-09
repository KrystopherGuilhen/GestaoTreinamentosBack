package gestao.treinamento.model.dto.cadastros;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeDTO {

    private Long id;
    private String nome;
    private String gerenteResponsavel;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String endereco;
    private String credenciamento;

    private Long idResponsavelTecnicoVinculo;
    private List<String> nomeResponsavelTecnicoVinculo;

    private Long idPerfilVinculo;
    private List<String> nomePerfilVinculo;

    private List<UnidadeAssinaturaDTO> assinatura;
}