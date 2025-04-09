package gestao.treinamento.model.dto.consultas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaConsultaDTO {


    private Long id;
    private String nome;
    private String cnpj;
    private String cpf;
}
