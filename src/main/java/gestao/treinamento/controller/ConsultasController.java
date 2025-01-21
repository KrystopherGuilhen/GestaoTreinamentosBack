package gestao.treinamento.controller;

import gestao.treinamento.model.entidade.Empresa;
import gestao.treinamento.service.ConsultaEmpresasService;
import gestao.treinamento.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@AllArgsConstructor
@CrossOrigin
public class ConsultasController {

    @Autowired
    private ConsultaEmpresasService serviceEmpresas;

    // GET: Buscar todos os instrutores
    @GetMapping("/empresas")
    public ResponseEntity<ApiResponse<List<Empresa>>> consultaEmpresa() {
        List<Empresa> empresas = serviceEmpresas.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Empresas recuperados com sucesso", empresas));
    }
}
