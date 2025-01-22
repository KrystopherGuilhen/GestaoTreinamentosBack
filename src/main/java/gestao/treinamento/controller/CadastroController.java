package gestao.treinamento.controller;

import gestao.treinamento.model.dto.TrabalhadorDTO;
import gestao.treinamento.model.entidade.Instrutor;
import gestao.treinamento.service.CadastroInstrutoresService;
import gestao.treinamento.service.CadastroTrabalhadoresService;
import gestao.treinamento.util.ApiResponse;
import jakarta.validation.Valid;
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
    private CadastroTrabalhadoresService serviceTrabalhadores;


    // GET: Buscar todos os instrutores
    @GetMapping("/instrutores")
    public ResponseEntity<ApiResponse<List<Instrutor>>> cadastroInstrutor() {
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

    // GET: Buscar todos os trabalhadores
    @GetMapping("/trabalhadores")
    public ResponseEntity<ApiResponse<List<TrabalhadorDTO>>> getTodosTrabalhadores() {
        List<TrabalhadorDTO> trabalhadores = serviceTrabalhadores.consultaCadastro();
        ApiResponse<List<TrabalhadorDTO>> response = new ApiResponse<>(true, "Trabalhadores recuperados com sucesso", trabalhadores);
        return ResponseEntity.ok(response);
    }

    // POST: Criar novo trabalhador
    @PostMapping("/trabalhadores")
    public ResponseEntity<ApiResponse<TrabalhadorDTO>> criarTrabalhador(@RequestBody @Valid TrabalhadorDTO trabalhadorDTO) {
        TrabalhadorDTO trabalhadorCriado = serviceTrabalhadores.criarTrabalhador(trabalhadorDTO);
        ApiResponse<TrabalhadorDTO> response = new ApiResponse<>(true, "Trabalhador criado com sucesso", trabalhadorCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT: Atualizar trabalhador por ID
    @PutMapping("trabalhadores/{id}")
    public ResponseEntity<ApiResponse<TrabalhadorDTO>> atualizarTrabalhador(
            @PathVariable Long id,
            @RequestBody @Valid TrabalhadorDTO trabalhadorDTO) {
        TrabalhadorDTO trabalhadorAtualizado = serviceTrabalhadores.atualizarTrabalhador(id, trabalhadorDTO);
        ApiResponse<TrabalhadorDTO> response = new ApiResponse<>(true, "Trabalhador atualizado com sucesso", trabalhadorAtualizado);
        return ResponseEntity.ok(response);
    }

    // DELETE: Excluir trabalhador por ID
    @DeleteMapping("trabalhadores/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirTrabalhador(@PathVariable Long id) {
        serviceTrabalhadores.excluirTrabalhador(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Trabalhador excluído com sucesso", null);
        return ResponseEntity.ok(response);
    }

    // DELETE: Excluir múltiplos trabalhadores por lista de IDs
    @DeleteMapping("/trabalhadores")
    public ResponseEntity<ApiResponse<Void>> excluirTrabalhadores(@RequestBody List<Long> ids) {
        serviceTrabalhadores.excluirTrabalhadores(ids);
        ApiResponse<Void> response = new ApiResponse<>(true, "Trabalhadores excluídos com sucesso", null);
        return ResponseEntity.ok(response);
    }


}