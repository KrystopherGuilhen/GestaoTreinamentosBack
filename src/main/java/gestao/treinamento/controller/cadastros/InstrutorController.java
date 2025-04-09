package gestao.treinamento.controller.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.InstrutorDTO;
import gestao.treinamento.service.cadastros.CadastroInstrutoresService;
import gestao.treinamento.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros/instrutores")
@AllArgsConstructor
@CrossOrigin
public class InstrutorController {

    private final CadastroInstrutoresService serviceInstrutores;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InstrutorDTO>>> getInstrutores() {
        List<InstrutorDTO> instrutores = serviceInstrutores.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Instrutores recuperados com sucesso", instrutores));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InstrutorDTO>> criarInstrutor(@RequestBody @Valid InstrutorDTO dto) {
        try {
            InstrutorDTO novoInstrutor = serviceInstrutores.criarInstrutor(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Instrutor criado com sucesso", novoInstrutor));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao criar o instrutor", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InstrutorDTO>> atualizarInstrutor(
            @PathVariable Long id, @RequestBody @Valid InstrutorDTO instrutorDTO) {
        try {
            InstrutorDTO instrutorAtualizado = serviceInstrutores.atualizarInstrutor(id, instrutorDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Instrutor atualizado com sucesso", instrutorAtualizado));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao atualizar o Instrutor", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarInstrutor(@PathVariable Long id) {
        try {
            serviceInstrutores.deletarInstrutor(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Instrutor deletado com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar o Instrutor", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletarInstrutores(@RequestBody List<Long> ids) {
        try {
            serviceInstrutores.deletarInstrutores(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Instrutores deletados com sucesso\"", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar os Instrutores", null));
        }
    }
}
