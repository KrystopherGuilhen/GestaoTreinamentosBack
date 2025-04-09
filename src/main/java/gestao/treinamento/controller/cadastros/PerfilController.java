package gestao.treinamento.controller.cadastros;

import gestao.treinamento.model.dto.cadastros.PerfilDTO;
import gestao.treinamento.service.cadastros.CadastroPerfilService;
import gestao.treinamento.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros/perfis")
@AllArgsConstructor
@CrossOrigin
public class PerfilController {

    private final CadastroPerfilService servicePerfis;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PerfilDTO>>> getPerfis() {
        List<PerfilDTO> perfis = servicePerfis.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Perfis recuperados com sucesso", perfis));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PerfilDTO>> criarPerfil(@RequestBody PerfilDTO perfil) {
        PerfilDTO novaPerfil = servicePerfis.criarPerfil(perfil);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Perfil criado com sucesso", novaPerfil));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PerfilDTO>> atualizarPerfil(@PathVariable Long id, @RequestBody PerfilDTO perfil) {
        PerfilDTO perfilAtualizado = servicePerfis.atualizarPerfil(id, perfil);
        return ResponseEntity.ok(new ApiResponse<>(true, "Perfil atualizado com sucesso", perfilAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarPerfil(@PathVariable Long id) {
        servicePerfis.deletarPerfil(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Perfil deletado com sucesso", null));
    }

    @DeleteMapping("/multiple")
    public ResponseEntity<ApiResponse<Void>> deletarPerfis(@RequestBody List<Long> ids) {
        servicePerfis.deletarPerfils(ids);
        return ResponseEntity.ok(new ApiResponse<>(true, "Perfis deletados com sucesso", null));
    }
}
