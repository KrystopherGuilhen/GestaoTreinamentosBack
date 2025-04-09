package gestao.treinamento.controller.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.EmpresaDTO;
import gestao.treinamento.service.cadastros.CadastroEmpresasService;
import gestao.treinamento.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros/empresas")
@AllArgsConstructor
@CrossOrigin
public class EmpresaController {

    private final CadastroEmpresasService serviceEmpresas;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmpresaDTO>>> getEmpresas() {
        List<EmpresaDTO> empresas = serviceEmpresas.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Empresas recuperadas com sucesso", empresas));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpresaDTO>> criarEmpresa(@RequestBody EmpresaDTO empresa) {
        EmpresaDTO novaEmpresa = serviceEmpresas.criarEmpresa(empresa);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Empresa criada com sucesso", novaEmpresa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpresaDTO>> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaDTO empresa) {
        EmpresaDTO empresaAtualizada = serviceEmpresas.atualizarEmpresa(id, empresa);
        return ResponseEntity.ok(new ApiResponse<>(true, "Empresa atualizada com sucesso", empresaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarEmpresa(@PathVariable Long id) {
        try {
            serviceEmpresas.deletarEmpresa(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Empresa deletada com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro ao deletar a empresa", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletarEmpresas(@RequestBody List<Long> ids) {
        try {
            serviceEmpresas.deletarEmpresas(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Empresas deletadas com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Erro ao deletar as empresas", null));
        }
    }
}
