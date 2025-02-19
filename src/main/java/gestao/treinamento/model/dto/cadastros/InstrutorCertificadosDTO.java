package gestao.treinamento.model.dto.cadastros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrutorCertificadosDTO {

    private String name;          // Nome original do arquivo
    private String mimeType;      // Tipo MIME (ex: "image/png")
    private String type;          // Categoria (opcional, ex: "PDF")
    private String objectURL;     // URL temporária (não persistida)
    private String base64;       // Dados no formato data:[mime];base64,...
    private Long size;          // Tamanho do arquivo...
}
