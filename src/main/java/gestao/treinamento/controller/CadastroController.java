package gestao.treinamento.controller;

import gestao.treinamento.model.entidade.Instrutor;
import gestao.treinamento.service.CadastroInstrutoresService;
import gestao.treinamento.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros")
@AllArgsConstructor
@CrossOrigin
public class CadastroController {

    @Autowired
    private CadastroInstrutoresService serviceInstrutores;

    // GET: Buscar todos os instrutores
    @GetMapping("/instrutores")
    public ResponseEntity<ApiResponse<List<Instrutor>>> cadastroProfessor() {
        List<Instrutor> instrutores = serviceInstrutores.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Instrutores recuperados com sucesso", instrutores));
    }

    // POST: Criar um novo instrutor
    @PostMapping("/instrutores")
    public ResponseEntity<ApiResponse<Instrutor>> criarInstrutor(@RequestBody Instrutor instrutor) {
        Instrutor novoInstrutor = serviceInstrutores.salvarInstrutor(instrutor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Instrutor criado com sucesso", novoInstrutor));
    }

    // PUT: Atualizar um instrutor existente
    @PutMapping("/instrutores/{id}")
    public ResponseEntity<ApiResponse<Instrutor>> atualizarInstrutor(@PathVariable Long id, @RequestBody Instrutor instrutor) {
        Instrutor instrutorAtualizado = serviceInstrutores.atualizarInstrutor(id, instrutor);
        return ResponseEntity.ok(new ApiResponse<>(true, "Instrutor atualizado com sucesso", instrutorAtualizado));
    }

    // DELETE: Deletar um instrutor pelo ID
    @DeleteMapping("/instrutores/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarInstrutor(@PathVariable Long id) {
        serviceInstrutores.deletarInstrutor(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Instrutor deletado com sucesso", null));
    }

    // DELETE múltiplo: Deletar vários instrutores pelos IDs
    @DeleteMapping("/instrutores")
    public ResponseEntity<ApiResponse<Void>> deletarInstrutores(@RequestBody List<Long> ids) {
        serviceInstrutores.deletarInstrutores(ids);
        return ResponseEntity.ok(new ApiResponse<>(true, "Instrutores deletados com sucesso", null));
    }


}