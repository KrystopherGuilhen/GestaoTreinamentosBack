package gestao.treinamento.controller;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.*;
import gestao.treinamento.model.entidades.Instrutor;
import gestao.treinamento.service.cadastros.*;
import gestao.treinamento.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cadastros")
@AllArgsConstructor
@CrossOrigin
public class CadastroController {

    @Autowired
    private CadastroInstrutoresService serviceInstrutores;
    private CadastroTrabalhadoresService serviceTrabalhadores;
    private CadastroEmpresasService serviceEmpresas;
    private CadastroUnidadesService serviceUnidades;
    private CadastroCursosService serviceCursos;
    private CadastroPalestrasService servicePalestras;
    private CadastroTurmasService serviceTurmas;
    private CadastroPerfilService servicePerfis;
    private CadastroResponsavelTecnicoService serviceResponsavelTecnico;


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

    // Endpoint para criação via arquivo Excel
    @PostMapping(value = "/trabalhadores/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<TrabalhadorDTO>>> uploadTrabalhadores(@RequestParam("file") MultipartFile file) {
        // Validação do nome do arquivo: remove a extensão (caso exista) e compara com "SesiTrabalhador"
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null ||
                !originalFilename.replaceFirst("[.][^.]+$", "").equals("SesiTrabalhador")) {
            ApiResponse<List<TrabalhadorDTO>> response = new ApiResponse<>(
                    false,
                    "Nome de arquivo inválido. O arquivo deve ter o nome 'SesiTrabalhador'.",
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        List<TrabalhadorDTO> createdTrabalhadores = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Supomos que a primeira linha é o cabeçalho, começando os dados na linha 1
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                TrabalhadorDTO dto = new TrabalhadorDTO();

                // Ajuste os índices conforme a ordem das colunas na sua planilha.
                dto.setNome(getCellValueAsString(row.getCell(0)));
                dto.setCidade(getCellValueAsString(row.getCell(1)));
                dto.setEstado(getCellValueAsString(row.getCell(2)));
                dto.setTelefone(getCellValueAsString(row.getCell(3)));
                dto.setCpf(getCellValueAsString(row.getCell(4)));
                dto.setRg(getCellValueAsString(row.getCell(5)));
                dto.setDataNascimento(getCellValueAsString(row.getCell(6)));
                dto.setEmail(getCellValueAsString(row.getCell(7)));

                // Se houver informações de empresa vinculada, supondo que:
                // Coluna 8: IDs das empresas (separados por vírgula)
                // Coluna 9: Nomes das empresas (separados por vírgula)
                if (row.getLastCellNum() > 8) {
                    String idEmpStr = getCellValueAsString(row.getCell(8));
                    if (idEmpStr != null && !idEmpStr.isEmpty()) {
                        List<Long> ids = Arrays.stream(idEmpStr.split(","))
                                .map(String::trim)
                                .map(Long::parseLong)
                                .collect(Collectors.toList());
                        dto.setIdEmpresaVinculo(ids);
                    }
                }
                if (row.getLastCellNum() > 9) {
                    String nomeEmpStr = getCellValueAsString(row.getCell(9));
                    if (nomeEmpStr != null && !nomeEmpStr.isEmpty()) {
                        List<String> nomes = Arrays.stream(nomeEmpStr.split(","))
                                .map(String::trim)
                                .collect(Collectors.toList());
                        dto.setNomeEmpresaVinculo(nomes);
                    }
                }

                // Chama o serviço para criar o trabalhador a partir do DTO
                TrabalhadorDTO created = serviceTrabalhadores.criarTrabalhador(dto);
                createdTrabalhadores.add(created);
            }
            workbook.close();

            ApiResponse<List<TrabalhadorDTO>> response = new ApiResponse<>(true, "Upload e processamento realizados com sucesso", createdTrabalhadores);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<List<TrabalhadorDTO>> response = new ApiResponse<>(false, "Erro ao processar o arquivo: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Método auxiliar para converter o valor de uma célula para String
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Formata a data para "dd/MM/yyyy" (ou outro formato desejado)
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    // Se for um número inteiro, evitar notação científica
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue); // Converte para long
                    } else {
                        // Para números decimais, usa formatação adequada
                        DecimalFormat df = new DecimalFormat("#.##");
                        return df.format(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue()); // Tenta obter como número
                } catch (IllegalStateException e) {
                    return cell.getCellFormula(); // Se não for possível, retorna a fórmula
                }
            default:
                return "";
        }
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

    // GET: Buscar todas as empresa
    @GetMapping("/empresas")
    public ResponseEntity<ApiResponse<List<EmpresaDTO>>> cadastroEmpresa() {
        List<EmpresaDTO> empresas = serviceEmpresas.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Empresas recuperadas com sucesso", empresas));
    }

    // POST: Criar um nova empresa
    @PostMapping("/empresas")
    public ResponseEntity<ApiResponse<EmpresaDTO>> criarEmpresas(@RequestBody EmpresaDTO empresa) {
        EmpresaDTO novaEmpresa = serviceEmpresas.criarEmpresa(empresa);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Empresa criada com sucesso", novaEmpresa));
    }

    // PUT: Atualizar uma empresa existente
    @PutMapping("/empresas/{id}")
    public ResponseEntity<ApiResponse<EmpresaDTO>> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaDTO empresa) {
        EmpresaDTO empresaAtualizada = serviceEmpresas.atualizarEmpresa(id, empresa);
        return ResponseEntity.ok(new ApiResponse<>(true, "Empresa atualizada com sucesso", empresaAtualizada));
    }

    // DELETE: Deletar uma empresa pelo ID
    @DeleteMapping("/empresas/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarEmpresa(@PathVariable Long id) {
        try {
            serviceEmpresas.deletarEmpresa(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Empresa deletada com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Erro interno ao deletar a empresa", null));
        }
    }

    // DELETE múltiplo: Deletar várias empresas pelos IDs
    @DeleteMapping("/empresas")
    public ResponseEntity<ApiResponse<Void>> deletarEmpresas(@RequestBody List<Long> ids) {
        try {
            serviceEmpresas.deletarEmpresas(ids);
            return ResponseEntity.ok(new ApiResponse<>(true, "Empresas deletadas com sucesso", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Erro interno ao deletar as empresas", null));
        }
    }

    // GET: Buscar todas as unidade
    @GetMapping("/unidades")
    public ResponseEntity<ApiResponse<List<UnidadeDTO>>> cadastrounidade() {
        List<UnidadeDTO> unidades = serviceUnidades.consultaCadastro();
        return ResponseEntity.ok(new ApiResponse<>(true, "Unidades" +
                " recuperadas com sucesso", unidades));
    }

    // POST: Criar uma nova unidade
    @PostMapping("/unidades")
    public ResponseEntity<ApiResponse<UnidadeDTO>> criarUnidade(@RequestBody UnidadeDTO unidade) {
        UnidadeDTO novaUnidade = serviceUnidades.criarUnidade(unidade);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Unidade criado com sucesso", novaUnidade));
    }

    // PUT: Atualizar uma unidade existente
    @PutMapping("/unidades/{id}")
    public ResponseEntity<ApiResponse<UnidadeDTO>> atualizarUnidade(@PathVariable Long id, @RequestBody UnidadeDTO unidade) {
        UnidadeDTO unidadeAtualizada = serviceUnidades.atualizarUnidade(id, unidade);
        return ResponseEntity.ok(new ApiResponse<>(true, "Unidade atualizado com sucesso", unidadeAtualizada));
    }

    // DELETE: Deletar uma unidade pelo ID
    @DeleteMapping("/unidades/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarUnidade(@PathVariable Long id) {
        serviceUnidades.deletarUnidade(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Unidade deletado com sucesso", null));
    }

    // DELETE múltiplo: Deletar varias unidades pelos IDs
    @DeleteMapping("/unidades")
    public ResponseEntity<ApiResponse<Void>> deletarUnidades(@RequestBody List<Long> ids) {
        serviceUnidades.deletarUnidades(ids);
        return ResponseEntity.ok(new ApiResponse<>(true, "Unidades deletadas com sucesso", null));
    }

    // GET: Buscar todos os cursos
    @GetMapping("/cursos")
    public ResponseEntity<ApiResponse<List<CursoDTO>>> getTodosCursos() {
        List<CursoDTO> cursos = serviceCursos.consultaCadastro();
        ApiResponse<List<CursoDTO>> response = new ApiResponse<>(true, "cursos recuperados com sucesso", cursos);
        return ResponseEntity.ok(response);
    }

    // POST: Criar novo curso
    @PostMapping("/cursos")
    public ResponseEntity<ApiResponse<CursoDTO>> criarCurso(@RequestBody @Valid CursoDTO cursoDTO) {
        CursoDTO cursoCriado = serviceCursos.criarCurso(cursoDTO);
        ApiResponse<CursoDTO> response = new ApiResponse<>(true, "Curso criado com sucesso", cursoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT: Atualizar curso por ID
    @PutMapping("cursos/{id}")
    public ResponseEntity<ApiResponse<CursoDTO>> atualizarCurso(
            @PathVariable Long id,
            @RequestBody @Valid CursoDTO cursoDTO) {
        CursoDTO cursoAtualizado = serviceCursos.atualizarCurso(id, cursoDTO);
        ApiResponse<CursoDTO> response = new ApiResponse<>(true, "Curso atualizado com sucesso", cursoAtualizado);
        return ResponseEntity.ok(response);
    }

    // DELETE: Excluir curso por ID
    @DeleteMapping("cursos/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirCurso(@PathVariable Long id) {
        serviceCursos.excluirCurso(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Curso excluído com sucesso", null);
        return ResponseEntity.ok(response);
    }

    // DELETE: Excluir múltiplos cursos por lista de IDs
    @DeleteMapping("/cursos")
    public ResponseEntity<ApiResponse<Void>> excluirCursos(@RequestBody List<Long> ids) {
        serviceCursos.excluirCursos(ids);
        ApiResponse<Void> response = new ApiResponse<>(true, "Cursos excluídos com sucesso", null);
        return ResponseEntity.ok(response);
    }

    // GET: Buscar todos os palestra
    @GetMapping("/palestras")
    public ResponseEntity<ApiResponse<List<PalestraDTO>>> getTodosPalestra() {
        List<PalestraDTO> palestra = servicePalestras.consultaCadastro();
        ApiResponse<List<PalestraDTO>> response = new ApiResponse<>(true, "Palestra recuperados com sucesso", palestra);
        return ResponseEntity.ok(response);
    }

    // POST: Criar novo palestra
    @PostMapping("/palestras")
    public ResponseEntity<ApiResponse<PalestraDTO>> criarPalestra(@RequestBody @Valid PalestraDTO palestraDTO) {
        PalestraDTO palestraCriado = servicePalestras.criarPalestra(palestraDTO);
        ApiResponse<PalestraDTO> response = new ApiResponse<>(true, "Palestra criado com sucesso", palestraCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT: Atualizar palestra por ID
    @PutMapping("palestras/{id}")
    public ResponseEntity<ApiResponse<PalestraDTO>> atualizarPalestra(
            @PathVariable Long id,
            @RequestBody @Valid PalestraDTO palestraDTO) {
        PalestraDTO palestraAtualizado = servicePalestras.atualizarPalestra(id, palestraDTO);
        ApiResponse<PalestraDTO> response = new ApiResponse<>(true, "Palestra atualizado com sucesso", palestraAtualizado);
        return ResponseEntity.ok(response);
    }

    // DELETE: Excluir palestra por ID
    @DeleteMapping("palestras/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirPalestra(@PathVariable Long id) {
        servicePalestras.excluirPalestra(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Palestra excluído com sucesso", null);
        return ResponseEntity.ok(response);
    }

    // DELETE: Excluir múltiplos palestras por lista de IDs
    @DeleteMapping("/palestras")
    public ResponseEntity<ApiResponse<Void>> excluirPalestras(@RequestBody List<Long> ids) {
        servicePalestras.excluirPalestras(ids);
        ApiResponse<Void> response = new ApiResponse<>(true, "Palestras excluídos com sucesso", null);
        return ResponseEntity.ok(response);
    }

    // GET: Buscar todos os turma
    @GetMapping("/turmas")
    public ResponseEntity<ApiResponse<List<TurmaDTO>>> getTodosTurma() {
        List<TurmaDTO> turma = serviceTurmas.consultaCadastro();
        ApiResponse<List<TurmaDTO>> response = new ApiResponse<>(true, "Turma recuperados com sucesso", turma);
        return ResponseEntity.ok(response);
    }

    // POST: Criar novo turma
    @PostMapping("/turmas")
    public ResponseEntity<ApiResponse<TurmaDTO>> criarTurma(@RequestBody @Valid TurmaDTO turmaDTO) {
        TurmaDTO turmaCriado = serviceTurmas.criarTurma(turmaDTO);
        ApiResponse<TurmaDTO> response = new ApiResponse<>(true, "Turma criado com sucesso", turmaCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT: Atualizar turma por ID
    @PutMapping("turmas/{id}")
    public ResponseEntity<ApiResponse<TurmaDTO>> atualizarTurma(
            @PathVariable Long id,
            @RequestBody @Valid TurmaDTO turmaDTO) {
        TurmaDTO turmaAtualizado = serviceTurmas.atualizarTurma(id, turmaDTO);
        ApiResponse<TurmaDTO> response = new ApiResponse<>(true, "Turma atualizado com sucesso", turmaAtualizado);
        return ResponseEntity.ok(response);
    }

    // DELETE: Excluir turma por ID
    @DeleteMapping("turmas/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirTurma(@PathVariable Long id) {
        serviceTurmas.excluirTurma(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Turma excluído com sucesso", null);
        return ResponseEntity.ok(response);
    }

    // DELETE: Excluir múltiplos turmas por lista de IDs
    @DeleteMapping("/turmas")
    public ResponseEntity<ApiResponse<Void>> excluirTurmas(@RequestBody List<Long> ids) {
        serviceTurmas.excluirTurmas(ids);
        ApiResponse<Void> response = new ApiResponse<>(true, "Turmas excluídos com sucesso", null);
        return ResponseEntity.ok(response);
    }

    // GET: Buscar todos os perfils
    @GetMapping("/perfis")
    public ResponseEntity<ApiResponse<List<PerfilDTO>>> getTodosPerfil() {
        List<PerfilDTO> perfil = servicePerfis.consultaCadastro();
        ApiResponse<List<PerfilDTO>> response = new ApiResponse<>(true, "Perfis recuperados com sucesso", perfil);
        return ResponseEntity.ok(response);
    }

    // POST: Criar uma nova perfil
    @PostMapping("/perfis")
    public ResponseEntity<ApiResponse<PerfilDTO>> criarPerfil(@RequestBody PerfilDTO perfil) {
        PerfilDTO novaPerfil = servicePerfis.criarPerfil(perfil);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Perfil criado com sucesso", novaPerfil));
    }

    // PUT: Atualizar uma perfil existente
    @PutMapping("/perfis/{id}")
    public ResponseEntity<ApiResponse<PerfilDTO>> atualizarPerfil(@PathVariable Long id, @RequestBody PerfilDTO perfil) {
        PerfilDTO perfilAtualizada = servicePerfis.atualizarPerfil(id, perfil);
        return ResponseEntity.ok(new ApiResponse<>(true, "Perfil atualizado com sucesso", perfilAtualizada));
    }

    // DELETE: Deletar uma perfil pelo ID
    @DeleteMapping("/perfis/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarPerfil(@PathVariable Long id) {
        servicePerfis.deletarPerfil(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Perfil deletado com sucesso", null));
    }

    // DELETE múltiplo: Deletar varias perfils pelos IDs
    @DeleteMapping("/perfils")
    public ResponseEntity<ApiResponse<Void>> deletarPerfils(@RequestBody List<Long> ids) {
        servicePerfis.deletarPerfils(ids);
        return ResponseEntity.ok(new ApiResponse<>(true, "Perfis deletadas com sucesso", null));
    }

    // GET: Buscar todos os responsavelTecnicos
    @GetMapping("/responsavelTecnico")
    public ResponseEntity<ApiResponse<List<ResponsavelTecnicoDTO>>> getTodosResponsavelTecnico() {
        List<ResponsavelTecnicoDTO> responsavelTecnico = serviceResponsavelTecnico.consultaCadastro();
        ApiResponse<List<ResponsavelTecnicoDTO>> response = new ApiResponse<>(true, "Responsáveis Técnicos recuperados com sucesso", responsavelTecnico);
        return ResponseEntity.ok(response);
    }

    // POST: Criar uma nova responsavelTecnico
    @PostMapping("/responsavelTecnico")
    public ResponseEntity<ApiResponse<ResponsavelTecnicoDTO>> criarResponsavelTecnico(@RequestBody ResponsavelTecnicoDTO responsavelTecnico) {
        ResponsavelTecnicoDTO novaResponsavelTecnico = serviceResponsavelTecnico.criarResponsavelTecnico(responsavelTecnico);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Responsável Técnico criado com sucesso", novaResponsavelTecnico));
    }

    // PUT: Atualizar uma responsavelTecnico existente
    @PutMapping("/responsavelTecnico/{id}")
    public ResponseEntity<ApiResponse<ResponsavelTecnicoDTO>> atualizarResponsavelTecnico(@PathVariable Long id, @RequestBody ResponsavelTecnicoDTO responsavelTecnico) {
        ResponsavelTecnicoDTO responsavelTecnicoAtualizada = serviceResponsavelTecnico.atualizarResponsavelTecnico(id, responsavelTecnico);
        return ResponseEntity.ok(new ApiResponse<>(true, "Responsável Técnico atualizado com sucesso", responsavelTecnicoAtualizada));
    }

    // DELETE: Deletar uma responsavelTecnico pelo ID
    @DeleteMapping("/responsavelTecnico/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarResponsavelTecnico(@PathVariable Long id) {
        serviceResponsavelTecnico.deletarResponsavelTecnico(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Responsável Técnico deletado com sucesso", null));
    }

    // DELETE múltiplo: Deletar varias responsavelTecnicos pelos IDs
    @DeleteMapping("/responsavelTecnico")
    public ResponseEntity<ApiResponse<Void>> deletarResponsavelTecnicos(@RequestBody List<Long> ids) {
        serviceResponsavelTecnico.deletarResponsavelTecnicos(ids);
        return ResponseEntity.ok(new ApiResponse<>(true, "Responsáveis Técnicos deletadas com sucesso", null));
    }
}