package gestao.treinamento.controller.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.TurmaDTO;
import gestao.treinamento.service.cadastros.CadastroTurmasService;
import gestao.treinamento.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros/turmas")
@AllArgsConstructor
@CrossOrigin
public class TurmaController {

    private final CadastroTurmasService serviceTurmas;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TurmaDTO>>> getTurmas() {
        List<TurmaDTO> turmas = serviceTurmas.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Turmas recuperadas com sucesso", turmas));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TurmaDTO>> criarTurma(@RequestBody @Valid TurmaDTO turmaDTO) {
        try {
            TurmaDTO turmaCriada = serviceTurmas.criarTurma(turmaDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Turma criada com sucesso", turmaCriada));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao criar a Turma", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TurmaDTO>> atualizarTurma(@PathVariable Long id, @RequestBody @Valid TurmaDTO turmaDTO) {
        try {
            TurmaDTO turmaAtualizada = serviceTurmas.atualizarTurma(id, turmaDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turma atualizada com sucesso", turmaAtualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao atualizar a Turma", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirTurma(@PathVariable Long id) {
        try {
            serviceTurmas.excluirTurma(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turma deletada com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar o Responsável Técnico", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> excluirTurmas(@RequestBody List<Long> ids) {
        try {
            serviceTurmas.excluirTurmas(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turmas deletadas com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar as Turmas", null));
        }
    }
}
