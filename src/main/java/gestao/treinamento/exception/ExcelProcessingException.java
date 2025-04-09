package gestao.treinamento.exception;

import lombok.Data;

@Data
public class ExcelProcessingException extends RuntimeException {
    private final int linha;
    private final String campo;
    private final String valor;

    public ExcelProcessingException(int linha, String campo, String valor, String mensagem) {
        super(String.format("Erro na linha %d, campo '%s' ('%s'): %s",
                linha + 1, campo, valor, mensagem));
        this.linha = linha;
        this.campo = campo;
        this.valor = valor;
    }
}