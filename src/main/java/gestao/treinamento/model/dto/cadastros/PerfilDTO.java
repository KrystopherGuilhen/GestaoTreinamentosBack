package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {

    private Long id;
    private String nome;

    private Long idUnidadeVinculo;
    private List<String> nomeUnidadeVinculo;

    private List<Long> idPermissaoVinculo;
    private List<String> nomePermissaoVinculo;

    private List<Long> idPermissaoCursoVinculo;
    private List<String> nomePermissaoCursoVinculo;

    private List<Long> idPermissaoEmpresaVinculo;
    private List<String> nomePermissaoEmpresaVinculo;

    private List<Long> idPermissaoResponsavelTecnicoVinculo;
    private List<String> nomePermissaoResponsavelTecnicoVinculo;

    private List<Long> idPermissaoUnidadeVinculo;
    private List<String> nomePermissaoUnidadeVinculo;

    private List<Long> idPermissaoTrabalhadorVinculo;
    private List<String> nomePermissaoTrabalhadorVinculo;

    private List<Long> idPermissaoPalestraVinculo;
    private List<String> nomePermissaoPalestraVinculo;

    private List<Long> idPermissaoTurmaVinculo;
    private List<String> nomePermissaoTurmaVinculo;

    private List<Long> idPermissaoPerfilVinculo;
    private List<String> nomePermissaoPerfilVinculo;

    private List<Long> idPermissaoInstrutorVinculo;
    private List<String> nomePermissaoInstrutorVinculo;

}