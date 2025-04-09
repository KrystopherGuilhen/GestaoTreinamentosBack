package gestao.treinamento.controller.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.ResponsavelTecnicoDTO;
import gestao.treinamento.service.cadastros.CadastroResponsavelTecnicoService;
import gestao.treinamento.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros/responsavelTecnico")
@AllArgsConstructor
@CrossOrigin
public class ResponsavelTecnicoController {

    private final CadastroResponsavelTecnicoService serviceResponsavelTecnico;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponsavelTecnicoDTO>>> getResponsaveis() {
        List<ResponsavelTecnicoDTO> responsaveis = serviceResponsavelTecnico.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Responsáveis Técnicos recuperados com sucesso", responsaveis));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ResponsavelTecnicoDTO>> criarResponsavelTecnico(@RequestBody ResponsavelTecnicoDTO responsavelTecnico) {
        try {
            ResponsavelTecnicoDTO novaResponsavel = serviceResponsavelTecnico.criarResponsavelTecnico(responsavelTecnico);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Responsável Técnico criado com sucesso", novaResponsavel));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao criar o Responsável Técnico", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResponsavelTecnicoDTO>> atualizarResponsavelTecnico(
            @PathVariable Long id,
            @RequestBody ResponsavelTecnicoDTO responsavelTecnico) {
        try {
            ResponsavelTecnicoDTO responsavelAtualizado = serviceResponsavelTecnico.atualizarResponsavelTecnico(id, responsavelTecnico);
            return ResponseEntity.ok(new ApiResponse<>(true, "Responsável Técnico atualizado com sucesso", responsavelAtualizado));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro interno ao atualizar o Responsável Técnico", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarResponsavelTecnico(@PathVariable Long id) {
        try {
            serviceResponsavelTecnico.deletarResponsavelTecnico(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Responsável Técnico deletado com sucesso", null));
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
    public ResponseEntity<ApiResponse<Void>> deletarResponsaveisTecnicos(@RequestBody List<Long> ids) {
        try {
            serviceResponsavelTecnico.deletarResponsavelTecnicos(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Responsáveis Técnicos deletados com sucesso", null));
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
