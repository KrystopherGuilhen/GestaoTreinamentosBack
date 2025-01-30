package gestao.treinamento.controller;

import gestao.treinamento.model.entidade.*;
import gestao.treinamento.service.consultas.*;
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
    private ConsultaEventosService serviceEventos;
    private ConsultaModalidadesService serviceModalidades;
    private ConsultaTrabalhadoresService serviceTrabalhadores;
    private ConsultaInstrutoresService serviceInstrutores;

    // GET: Buscar todas as empresas
    @GetMapping("/empresas")
    public ResponseEntity<ApiResponse<List<Empresa>>> consultaEmpresa() {
        List<Empresa> empresas = serviceEmpresas.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Empresas recuperados com sucesso", empresas));
    }

    // GET: Buscar todos os eventos
    @GetMapping("/eventos")
    public ResponseEntity<ApiResponse<List<Evento>>> consultaEvento() {
        List<Evento> eventos = serviceEventos.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Eventos recuperados com sucesso", eventos));
    }

    // GET: Buscar todas as modalidades
    @GetMapping("/modalidades")
    public ResponseEntity<ApiResponse<List<Modalidade>>> consultaModalidade() {
        List<Modalidade> modalidades = serviceModalidades.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Modalidades recuperadas com sucesso", modalidades));
    }

    // GET: Buscar todos os instrutores
    @GetMapping("/trabalhadores")
    public ResponseEntity<ApiResponse<List<Trabalhador>>> consultaTrabalhador() {
        List<Trabalhador> trabalhadores = serviceTrabalhadores.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Trabalhadores recuperados com sucesso", trabalhadores));
    }

    // GET: Buscar todos os instrutores
    @GetMapping("/instrutores")
    public ResponseEntity<ApiResponse<List<Instrutor>>> consultaInstrutor() {
        List<Instrutor> instrutores = serviceInstrutores.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Instrutores recuperados com sucesso", instrutores));
    }
}
