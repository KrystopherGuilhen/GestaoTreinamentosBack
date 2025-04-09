package gestao.treinamento.controller.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.TrabalhadorDTO;
import gestao.treinamento.service.cadastros.CadastroTrabalhadoresService;
import gestao.treinamento.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cadastros/trabalhadores")
@AllArgsConstructor
@CrossOrigin
public class TrabalhadorController {

    private final CadastroTrabalhadoresService serviceTrabalhadores;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TrabalhadorDTO>>> getTodosTrabalhadores() {
        List<TrabalhadorDTO> trabalhadores = serviceTrabalhadores.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Trabalhadores recuperados com sucesso", trabalhadores));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrabalhadorDTO>> criarTrabalhador(@RequestBody @Valid TrabalhadorDTO trabalhadorDTO) {
        try {
            TrabalhadorDTO trabalhadorCriado = serviceTrabalhadores.criarTrabalhador(trabalhadorDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Trabalhador criado com sucesso", trabalhadorCriado));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao criar o Trabalhador", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrabalhadorDTO>> atualizarTrabalhador(
            @PathVariable Long id,
            @RequestBody @Valid TrabalhadorDTO trabalhadorDTO) {
        try {
            TrabalhadorDTO trabalhadorAtualizado = serviceTrabalhadores.atualizarTrabalhador(id, trabalhadorDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Trabalhador atualizado com sucesso", trabalhadorAtualizado));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao atualizar o Trabalhador", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirTrabalhador(@PathVariable Long id) {
        try {
            serviceTrabalhadores.excluirTrabalhador(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Trabalhador excluído com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar o Trabalhador", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> excluirTrabalhadores(@RequestBody List<Long> ids) {
        try {
            serviceTrabalhadores.excluirTrabalhadores(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Trabalhadores excluídos com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao deletar os Trabalhadores", null));
        }
    }

    // Exemplo de endpoint para upload via arquivo Excel
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<TrabalhadorDTO>>> uploadTrabalhadores(@RequestParam("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.replaceFirst("[.][^.]+$", "").equals("Trabalhador")) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(false, "Nome de arquivo inválido.", null)
                );
            }
            List<TrabalhadorDTO> createdTrabalhadores = serviceTrabalhadores.processarArquivoTrabalhadores(file);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Upload e processamento realizados com sucesso", createdTrabalhadores));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Erro ao processar o arquivo: " + e.getMessage(), null));
        }
    }
}
