package gestao.treinamento.controller;

import gestao.treinamento.model.dto.consultas.*;
import gestao.treinamento.model.entidades.*;
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
    private ConsultaCursosService serviceCursos;
    private ConsultaTurmasService serviceTurmas;

    // GET: Buscar todas as empresas
    @GetMapping("/empresas")
    public ResponseEntity<ApiResponse<List<EmpresaConsultaDTO>>> consultaEmpresa() {
        List<EmpresaConsultaDTO> empresas = serviceEmpresas.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Empresas consultadas com sucesso", empresas));
    }

    // GET: Buscar todos os eventos
    @GetMapping("/eventos")
    public ResponseEntity<ApiResponse<List<EventoConsultaDTO>>> consultaEvento() {
        List<EventoConsultaDTO> eventos = serviceEventos.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Eventos consultados com sucesso", eventos));
    }

    // GET: Buscar todas as modalidades
    @GetMapping("/modalidades")
    public ResponseEntity<ApiResponse<List<ModalidadeConsultaDTO>>> consultaModalidade() {
        List<ModalidadeConsultaDTO> modalidades = serviceModalidades.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Modalidades consultadas com sucesso", modalidades));
    }

    // GET: Buscar todos os instrutores
    @GetMapping("/trabalhadores")
    public ResponseEntity<ApiResponse<List<TrabalhadorConsultaDTO>>> consultaTrabalhador() {
        List<TrabalhadorConsultaDTO> trabalhadores = serviceTrabalhadores.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Trabalhadores consultados com sucesso", trabalhadores));
    }

    // GET: Buscar todos os instrutores
    @GetMapping("/instrutores")
    public ResponseEntity<ApiResponse<List<InstrutorConsultaDTO>>> consultaInstrutor() {
        List<InstrutorConsultaDTO> instrutores = serviceInstrutores.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Instrutores consultados com sucesso", instrutores));
    }

    // GET: Buscar todos os curso
    @GetMapping("/cursos")
    public ResponseEntity<ApiResponse<List<CursoConsultaDTO>>> consultaCurso() {
        List<CursoConsultaDTO> cursos = serviceCursos.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Cursos consultados com sucesso", cursos));
    }

    // GET: Buscar todos os turmas
    @GetMapping("/turmas")
    public ResponseEntity<ApiResponse<List<TurmaConsultaDTO>>> consultaTurma() {
        List<TurmaConsultaDTO> turmas = serviceTurmas.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Turmas consultadas com sucesso", turmas));
    }
}
