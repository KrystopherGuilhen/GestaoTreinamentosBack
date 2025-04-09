package gestao.treinamento.controller.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.PalestraDTO;
import gestao.treinamento.service.cadastros.CadastroPalestrasService;
import gestao.treinamento.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros/palestras")
@AllArgsConstructor
@CrossOrigin
public class PalestraController {

    private final CadastroPalestrasService servicePalestras;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PalestraDTO>>> getPalestras() {
        List<PalestraDTO> palestras = servicePalestras.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Palestras recuperadas com sucesso", palestras));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PalestraDTO>> criarPalestra(@RequestBody @Valid PalestraDTO palestraDTO) {
        try {
            PalestraDTO palestraCriada = servicePalestras.criarPalestra(palestraDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Palestra criada com sucesso", palestraCriada));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao criar a Palestra", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PalestraDTO>> atualizarPalestra(@PathVariable Long id, @RequestBody @Valid PalestraDTO palestraDTO) {
        try {
            PalestraDTO palestraAtualizada = servicePalestras.atualizarPalestra(id, palestraDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Palestra atualizada com sucesso", palestraAtualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao atualizar a Palestra", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirPalestra(@PathVariable Long id) {
        try {
            servicePalestras.excluirPalestra(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Palestra deletada com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar a Palestra", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> excluirPalestras(@RequestBody List<Long> ids) {
        try {
            servicePalestras.excluirPalestras(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Palestras deletadas com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar os Responsáveis Técnicos", null));
        }
    }
}
