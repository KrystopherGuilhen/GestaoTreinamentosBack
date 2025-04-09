package gestao.treinamento.controller.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.CursoDTO;
import gestao.treinamento.service.cadastros.CadastroCursosService;
import gestao.treinamento.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros/cursos")
@AllArgsConstructor
@CrossOrigin
public class CursoController {

    private final CadastroCursosService serviceCursos;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CursoDTO>>> getCursos() {
        List<CursoDTO> cursos = serviceCursos.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Cursos recuperados com sucesso", cursos));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CursoDTO>> criarCurso(@RequestBody @Valid CursoDTO cursoDTO) {
        CursoDTO cursoCriado = serviceCursos.criarCurso(cursoDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Curso criado com sucesso", cursoCriado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CursoDTO>> atualizarCurso(@PathVariable Long id, @RequestBody @Valid CursoDTO cursoDTO) {
        CursoDTO cursoAtualizado = serviceCursos.atualizarCurso(id, cursoDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Curso atualizado com sucesso", cursoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirCurso(@PathVariable Long id) {
        try {
            serviceCursos.excluirCurso(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Curso excluído com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro ao deletar o curso", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> excluirCursos(@RequestBody List<Long> ids) {
        try {
            serviceCursos.excluirCursos(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cursos excluídos com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro ao deletar os cursos", null));
        }
    }
}
