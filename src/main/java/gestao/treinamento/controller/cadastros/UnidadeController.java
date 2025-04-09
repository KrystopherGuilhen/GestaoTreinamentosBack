package gestao.treinamento.controller.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.UnidadeDTO;
import gestao.treinamento.service.cadastros.CadastroUnidadesService;
import gestao.treinamento.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros/unidades")
@AllArgsConstructor
@CrossOrigin
public class UnidadeController {

    private final CadastroUnidadesService serviceUnidades;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UnidadeDTO>>> getUnidades() {
        List<UnidadeDTO> unidades = serviceUnidades.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Unidades recuperadas com sucesso", unidades));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UnidadeDTO>> criarUnidade(@RequestBody UnidadeDTO unidade) {
        try {
            UnidadeDTO novaUnidade = serviceUnidades.criarUnidade(unidade);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Unidade criada com sucesso", novaUnidade));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao criar a Unidade", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UnidadeDTO>> atualizarUnidade(@PathVariable Long id, @RequestBody UnidadeDTO unidade) {
        try {
            UnidadeDTO unidadeAtualizada = serviceUnidades.atualizarUnidade(id, unidade);
            return ResponseEntity.ok(new ApiResponse<>(true, "Unidade atualizada com sucesso", unidadeAtualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao atualizar a Unidade", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarUnidade(@PathVariable Long id) {
        try {
            serviceUnidades.deletarUnidade(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Unidade deletada com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar a Unidade", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletarUnidades(@RequestBody List<Long> ids) {
        try {
            serviceUnidades.deletarUnidades(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Unidades deletadas com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar as Unidades", null));
        }
    }
}
