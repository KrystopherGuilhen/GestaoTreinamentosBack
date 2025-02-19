package gestao.treinamento.controller;

import gestao.treinamento.model.dto.consultas.*;
import gestao.treinamento.service.consultas.*;
import gestao.treinamento.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private ConsultaIndustriasService serviceIndustrias;
    private ConsultaResponsavelTecnicoService serviceResponsavelTecnico;
    private ConsultaUnidadesService serviceUnidade;
    private ConsultaNiveisPermissoesService serviceNivelPermissao;
    private ConsultaPermissaoCursoService servicePermissaoCurso;
    private ConsultaPermissaoEmpresaService servicePermissaoEmpresa;
    private ConsultaPermissaoInstrutorService servicePermissaoInstrutor;
    private ConsultaPermissaoPalestraService servicePermissaoPalestra;
    private ConsultaPermissaoPerfilService servicePermissaoPerfil;
    private ConsultaPermissaoResponsavelTecnicoService servicePermissaoResponsavelTecnico;
    private ConsultaPermissaoTrabalhadorService servicePermissaoTrabalhador;
    private ConsultaPermissaoTurmaService servicePermissaoTurma;
    private ConsultaPermissaoUnidadeService servicePermissaoUnidade;
    private ConsultaPessoaService servicePessoa;
    private ConsultaInstrutorFormacaoService serviceInstrutorFormacao;

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

    // GET: Buscar todos os trabalhadores
    @GetMapping("/trabalhadores")
    public ResponseEntity<ApiResponse<List<TrabalhadorConsultaDTO>>> consultaTrabalhador() {
        List<TrabalhadorConsultaDTO> trabalhadores = serviceTrabalhadores.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Trabalhadores consultados com sucesso", trabalhadores));
    }

    // GET: Buscar trabalhadores por múltiplas empresas (via query params)
    @GetMapping("/trabalhadores/empresas")
    public ResponseEntity<ApiResponse<List<TrabalhadorConsultaDTO>>> consultaTrabalhadorPorEmpresas(
            @RequestParam List<Long> empresaIds) {
        List<TrabalhadorConsultaDTO> trabalhadores = serviceTrabalhadores.consultaPorEmpresas(empresaIds);
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

    // GET: Buscar todos as turmas
    @GetMapping("/turmas")
    public ResponseEntity<ApiResponse<List<TurmaConsultaDTO>>> consultaTurma() {
        List<TurmaConsultaDTO> turmas = serviceTurmas.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Turmas consultadas com sucesso", turmas));
    }

    // GET: Buscar todos as industrias
    @GetMapping("/industrias")
    public ResponseEntity<ApiResponse<List<IndustriaConsultaDTO>>> consultaIndustria() {
        List<IndustriaConsultaDTO> industria = serviceIndustrias.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Industrias consultadas com sucesso", industria));
    }

    // GET: Buscar todos os ResponsaveisTecnicos
    @GetMapping("/responsaveisTecnicos")
    public ResponseEntity<ApiResponse<List<ResponsavelTecnicoConsultaDTO>>> consultaResponsavelTecnico() {
        List<ResponsavelTecnicoConsultaDTO> responsavelTecnico = serviceResponsavelTecnico.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Responsáveis Técnicos consultados com sucesso", responsavelTecnico));
    }

    // GET: Buscar todos os Unidades
    @GetMapping("/unidades")
    public ResponseEntity<ApiResponse<List<UnidadeConsultaDTO>>> consultaUnidade() {
        List<UnidadeConsultaDTO> unidade = serviceUnidade.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Unidades consultadas com sucesso", unidade));
    }

    // GET: Buscar todos os NivelPermissao
    @GetMapping("/niveisPermissoes")
    public ResponseEntity<ApiResponse<List<NivelPermissaoConsultaDTO>>> consultaNivelPermissao() {
        List<NivelPermissaoConsultaDTO> nivelPermissao = serviceNivelPermissao.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Niveis de permissões consultadas com sucesso", nivelPermissao));
    }

    // GET: Buscar todos os PermissaoCurso
    @GetMapping("/permissaoCurso")
    public ResponseEntity<ApiResponse<List<PermissaoCursoConsultaDTO>>> consultaPermissaoCurso() {
        List<PermissaoCursoConsultaDTO> permissaoCurso = servicePermissaoCurso.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Cursos consultadas com sucesso", permissaoCurso));
    }

    // GET: Buscar todos os PermissaoEmpresa
    @GetMapping("/permissaoEmpresa")
    public ResponseEntity<ApiResponse<List<PermissaoEmpresaConsultaDTO>>> consultaPermissaoEmpresa() {
        List<PermissaoEmpresaConsultaDTO> permissaoEmpresa = servicePermissaoEmpresa.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Empresas consultadas com sucesso", permissaoEmpresa));
    }

    // GET: Buscar todos os PermissaoInstrutor
    @GetMapping("/permissaoInstrutor")
    public ResponseEntity<ApiResponse<List<PermissaoInstrutorConsultaDTO>>> consultaPermissaoInstrutor() {
        List<PermissaoInstrutorConsultaDTO> permissaoInstrutor = servicePermissaoInstrutor.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Instrutores consultadas com sucesso", permissaoInstrutor));
    }

    // GET: Buscar todos os PermissaoPalestra
    @GetMapping("/permissaoPalestra")
    public ResponseEntity<ApiResponse<List<PermissaoPalestraConsultaDTO>>> consultaPermissaoPalestra() {
        List<PermissaoPalestraConsultaDTO> permissaoPalestra = servicePermissaoPalestra.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Palestras consultadas com sucesso", permissaoPalestra));
    }

    // GET: Buscar todos os PermissaoPerfil
    @GetMapping("/permissaoPerfil")
    public ResponseEntity<ApiResponse<List<PermissaoPerfilConsultaDTO>>> consultaPermissaoPerfil() {
        List<PermissaoPerfilConsultaDTO> permissaoPerfil = servicePermissaoPerfil.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Perfis consultadas com sucesso", permissaoPerfil));
    }

    // GET: Buscar todos os PermissaoResponsavelTecnico
    @GetMapping("/permissaoResponsavelTecnico")
    public ResponseEntity<ApiResponse<List<PermissaoResponsavelTecnicoConsultaDTO>>> consultaPermissaoResponsavelTecnico() {
        List<PermissaoResponsavelTecnicoConsultaDTO> permissaoResponsavelTecnico = servicePermissaoResponsavelTecnico.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Responsaveis Técnicos consultadas com sucesso", permissaoResponsavelTecnico));
    }

    // GET: Buscar todos os PermissaoTrabalhador
    @GetMapping("/permissaoTrabalhador")
    public ResponseEntity<ApiResponse<List<PermissaoTrabalhadorConsultaDTO>>> consultaPermissaoTrabalhador() {
        List<PermissaoTrabalhadorConsultaDTO> permissaoTrabalhador = servicePermissaoTrabalhador.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Trabalhadors consultadas com sucesso", permissaoTrabalhador));
    }

    // GET: Buscar todos os PermissaoTurma
    @GetMapping("/permissaoTurma")
    public ResponseEntity<ApiResponse<List<PermissaoTurmaConsultaDTO>>> consultaPermissaoTurma() {
        List<PermissaoTurmaConsultaDTO> permissaoTurma = servicePermissaoTurma.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Turmas consultadas com sucesso", permissaoTurma));
    }

    // GET: Buscar todos os PermissaoUnidade
    @GetMapping("/permissaoUnidade")
    public ResponseEntity<ApiResponse<List<PermissaoUnidadeConsultaDTO>>> consultaPermissaoUnidade() {
        List<PermissaoUnidadeConsultaDTO> permissaoUnidade = servicePermissaoUnidade.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Permissões de Unidades consultadas com sucesso", permissaoUnidade));
    }

    // GET: Buscar todas as TipoPessoa
    @GetMapping("/tipoPessoas")
    public ResponseEntity<ApiResponse<List<PessoaConsultaDTO>>> consultaTipoPessoa() {
        List<PessoaConsultaDTO> pessoa = servicePessoa.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Tipo pessoas consultadas com sucesso", pessoa));
    }

    // GET: Buscar as formações de um instrutor específico
    @GetMapping("/instrutorFormacoes")
    public ResponseEntity<ApiResponse<List<InstrutorFormacaoConsultaDTO>>> consultaInstrutorFormacao(
            @RequestParam Long instrutorId) {
        List<InstrutorFormacaoConsultaDTO> formacoes = serviceInstrutorFormacao.consultaPorInstrutor(instrutorId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Formações do instrutor consultadas com sucesso", formacoes));
    }
}
