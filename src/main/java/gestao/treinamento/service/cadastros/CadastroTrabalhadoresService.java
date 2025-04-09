package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ExcelProcessingException;
import gestao.treinamento.model.dto.cadastros.TrabalhadorDTO;
import gestao.treinamento.model.entidades.*;
import gestao.treinamento.repository.cadastros.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CadastroTrabalhadoresService {

    @Autowired
    private final CadastroTrabalhadoresRepository repository;
    private final CadastroEmpresasRepository empresaRepository;
    private final CadastroTrabalhadorEmpresaRepository trabalhadorEmpresaRepository;
    private final CadastroEstadosRepository estadosRepository;
    private final CadastroCidadesRepository cidadesRepository;

    // GET: Buscar todos os trabalhadores
    public List<TrabalhadorDTO> consultaCadastro() {
        List<Trabalhador> trabalhadores = repository.findAll();

        return trabalhadores.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo trabalhador
    @Transactional
    public TrabalhadorDTO criarTrabalhador(TrabalhadorDTO dto) {
        // Validações de campos únicos
        if (repository.existsByCpf(dto.getCpf())) {
            throw new DuplicateException("CPF já cadastrado: " + dto.getCpf());
        }
        if (repository.existsByRg(dto.getRg())) {
            throw new DuplicateException("RG já cadastrado: " + dto.getRg());
        }
        if (repository.existsByTelefone(dto.getTelefone())) {
            throw new DuplicateException("Telefone já cadastrado: " + dto.getTelefone());
        }
        if (StringUtils.hasText(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateException("E-mail já cadastrado: " + dto.getEmail());
        }


        // Converter o DTO para entidade Trabalhador
        Trabalhador trabalhador = convertToEntity(dto);

        // Salvar o trabalhador e obter o ID gerado
        trabalhador = repository.save(trabalhador);

        // Verificar se há empresas vinculadas no DTO
        if (dto.getIdEmpresaVinculo() != null && !dto.getIdEmpresaVinculo().isEmpty()) {
            for (Long idEmpresa : dto.getIdEmpresaVinculo()) {
                // Recuperar a empresa pelo ID (se necessário)
                Empresa empresa = empresaRepository.findById(idEmpresa)
                        .orElseThrow(() -> new RuntimeException("Empresa não encontrada: ID " + idEmpresa));

                // Criar a associação trabalhador-empresa
                TrabalhadorEmpresa trabalhadorEmpresas = new TrabalhadorEmpresa();
                trabalhadorEmpresas.setTrabalhador(trabalhador);
                trabalhadorEmpresas.setEmpresa(empresa);

                // Salvar a associação
                trabalhadorEmpresaRepository.save(trabalhadorEmpresas);
            }
        }

        // Retornar o DTO do trabalhador criado
        return convertToDTO(trabalhador);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<TrabalhadorDTO> processarArquivoTrabalhadores(MultipartFile file) throws IOException {
        List<TrabalhadorDTO> createdTrabalhadores = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (isRowEmpty(row)) {
                    break;
                }

                try {
                    TrabalhadorDTO dto = criarDtoAPartirDaRow(row, i + 1);
                    validarCamposObrigatorios(dto, i); // Valida RG, CPF, etc.
                    validarCamposUnicos(dto, i);        // Valida duplicatas

                    Trabalhador trabalhador = convertToEntity(dto);
                    trabalhador = repository.save(trabalhador);
                    createdTrabalhadores.add(convertToDTO(trabalhador));

                } catch (ExcelProcessingException ex) {
                    throw ex; // Repassa exceções específicas
                } catch (Exception ex) {
                    throw tratarErroGenerico(ex, i); // Trata apenas erros inesperados
                }
            }
        } catch (Exception ex) {
            // Remove este bloco para evitar sobrescrever a exceção!
            // A exceção já foi tratada internamente ou será propagada
            throw ex;
        }

        return createdTrabalhadores;
    }

    // PUT: Atualizar trabalhador existente
    @Transactional
    public TrabalhadorDTO atualizarTrabalhador(Long id, TrabalhadorDTO dto) {
        Trabalhador existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trabalhador com ID " + id + " não encontrado"));

        // Validações de campos únicos (ignorando o próprio registro)
        if (StringUtils.hasText(dto.getCpf()) && !dto.getCpf().equals(existente.getCpf())) {
            if (repository.existsByCpfAndIdNot(dto.getCpf(), id)) {
                throw new DuplicateException("CPF já cadastrado: " + dto.getCpf());
            }
            existente.setCpf(dto.getCpf());
        }

        if (StringUtils.hasText(dto.getRg()) && !dto.getRg().equals(existente.getRg())) {
            if (repository.existsByRgAndIdNot(dto.getRg(), id)) {
                throw new DuplicateException("RG já cadastrado: " + dto.getRg());
            }
            existente.setRg(dto.getRg());
        }

        if (StringUtils.hasText(dto.getTelefone()) && !dto.getTelefone().equals(existente.getTelefone())) {
            if (repository.existsByTelefoneAndIdNot(dto.getTelefone(), id)) {
                throw new DuplicateException("Telefone já cadastrado: " + dto.getTelefone());
            }
            existente.setTelefone(dto.getTelefone());
        }

        if (StringUtils.hasText(dto.getEmail()) && !dto.getEmail().equals(existente.getEmail())) {
            if (repository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new DuplicateException("E-mail já cadastrado: " + dto.getEmail());
            }
            existente.setEmail(dto.getEmail());
        }

        existente.setNome(dto.getNome());
        existente.setIdEstado(dto.getIdEstado());
        existente.setNomeEstado(dto.getNomeEstado());
        existente.setIdCidade(dto.getIdCidade());
        existente.setNomeCidade(dto.getNomeCidade());

        if (dto.getDataNascimento() != null) {
            String dataNascimentoStr = dto.getDataNascimento();
            LocalDate dataNascimento = null;

            try {
                // Primeiro, tenta converter do formato ISO 8601
                if (dataNascimentoStr.contains("T")) {
                    dataNascimento = LocalDate.parse(dataNascimentoStr.split("T")[0]);
                } else {
                    // Tenta converter do formato dd/MM/yyyy
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);
                }
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de data inválido: " + dataNascimentoStr);
            }

            existente.setDataNascimento(dataNascimento);
        }

        // Atualizar associações com empresas
        if (dto.getIdEmpresaVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTrabalhador e idEmpresa)
            List<Long> idsEmpresasVinculadas = trabalhadorEmpresaRepository.findEmpresasByTrabalhadorId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsEmpresasVinculadas.stream()
                    .filter(idEmpresa -> !dto.getIdEmpresaVinculo().contains(idEmpresa))
                    .toList();
            trabalhadorEmpresaRepository.deleteByTrabalhadorIdAndEmpresaIds(id, idsParaRemover);

            // Adicionar novas associações (para cada empresa que não existe na tabela)
            for (Long idEmpresa : dto.getIdEmpresaVinculo()) {
                boolean existe = trabalhadorEmpresaRepository.existsByTrabalhadorIdAndEmpresaId(id, idEmpresa);
                if (!existe) {
                    Empresa empresa = empresaRepository.findById(idEmpresa)
                            .orElseThrow(() -> new EntityNotFoundException("Empresa com ID " + idEmpresa + " não encontrada"));

                    TrabalhadorEmpresa novaAssociacao = new TrabalhadorEmpresa();
                    novaAssociacao.setTrabalhador(existente);
                    novaAssociacao.setEmpresa(empresa);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    trabalhadorEmpresaRepository.save(novaAssociacao);
                }
            }
        }

        Trabalhador trabalhadorAtualizado = repository.save(existente);
        return convertToDTO(trabalhadorAtualizado);
    }

    // DELETE: Excluir trabalhador por ID
    public void excluirTrabalhador(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Trabalhador com ID " + id + " não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Trabalhador não pode ser excluído pois está vinculado a outro cadastro");
        }
    }

    // DELETE: Excluir múltiplos trabalhadores por lista de IDs
    public void excluirTrabalhadores(List<Long> ids) {
        List<Trabalhador> trabalhadores = repository.findAllById(ids);
        if (trabalhadores.isEmpty()) {
            throw new EntityNotFoundException("Nenhum trabalhador encontrado para os IDs fornecidos");
        }
        try {
            repository.deleteAll(trabalhadores);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Um ou mais trabalhadores não podem ser excluídos pois estão vinculados a outros cadastros.");
        }

    }

    // Método auxiliar: Converter entidade para DTO
    private TrabalhadorDTO convertToDTO(Trabalhador trabalhador) {
        TrabalhadorDTO dto = new TrabalhadorDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dto.setId(trabalhador.getId());
        dto.setNome(trabalhador.getNome());
        dto.setIdEstado(trabalhador.getIdEstado());
        dto.setNomeEstado(trabalhador.getNomeEstado());
        dto.setIdCidade(trabalhador.getIdCidade());
        dto.setNomeCidade(trabalhador.getNomeCidade());
        dto.setTelefone(trabalhador.getTelefone());
        dto.setCpf(trabalhador.getCpf());
        dto.setRg(trabalhador.getRg());
        dto.setDataNascimento(
                trabalhador.getDataNascimento() != null
                        ? trabalhador.getDataNascimento().format(formatter)
                        : null
        );
        dto.setEmail(trabalhador.getEmail());

        // Extrai os IDs e nomes das empresas vinculadas
        List<Long> idEmpresas = trabalhador.getEmpresasVinculadas() != null
                ? trabalhador.getEmpresasVinculadas().stream()
                .map(te -> te.getEmpresa().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdEmpresaVinculo(idEmpresas);

        List<String> nomesEmpresas = trabalhador.getEmpresasVinculadas().stream()
                .map(te -> te.getEmpresa().getNome())
                .toList();

        dto.setIdEmpresaVinculo(idEmpresas);
        dto.setNomeEmpresaVinculo(nomesEmpresas);

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Trabalhador convertToEntity(TrabalhadorDTO dto) {
        Trabalhador trabalhador = new Trabalhador();
        trabalhador.setNome(dto.getNome());
        trabalhador.setIdEstado(dto.getIdEstado());
        trabalhador.setNomeEstado(dto.getNomeEstado());
        trabalhador.setIdCidade(dto.getIdCidade());
        trabalhador.setNomeCidade(dto.getNomeCidade());
        trabalhador.setTelefone(dto.getTelefone());
        trabalhador.setCpf(dto.getCpf());
        trabalhador.setRg(dto.getRg());

        // **Chama o método de conversão de data**
        if (dto.getDataNascimento() != null && !dto.getDataNascimento().isEmpty()) {
            trabalhador.setDataNascimento(converterData(dto.getDataNascimento()));
        }

        trabalhador.setEmail(dto.getEmail());

        return trabalhador;
    }

    // Método para converter data em diferentes formatos
    private LocalDate converterData(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        // Formato 1: "Sun Oct 01 00:00:00 GMT-04:00 2000"
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy", Locale.ENGLISH);

        // Formato 2: "01/10/2000"
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            return ZonedDateTime.parse(data, formatter1).toLocalDate();
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(data, formatter2);
            } catch (DateTimeParseException e2) {
                System.out.println("Erro ao converter data: " + data);
                return null;
            }
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private TrabalhadorDTO criarDtoAPartirDaRow(Row row, int linha) {
        TrabalhadorDTO dto = new TrabalhadorDTO();

        // Mapeamento das células (ajuste os índices conforme sua planilha)
        dto.setNome(getCellValueAsString(row.getCell(0)));
        dto.setNomeCidade(getCellValueAsString(row.getCell(1)));
        dto.setNomeEstado(getCellValueAsString(row.getCell(2)));
        dto.setTelefone(getCellValueAsString(row.getCell(3)));
        dto.setCpf(getCellValueAsString(row.getCell(4)));
        dto.setRg(getCellValueAsString(row.getCell(5)));
        dto.setDataNascimento(getCellValueAsString(row.getCell(6)));
        dto.setEmail(getCellValueAsString(row.getCell(7)));

        // Buscar ID do Estado pela Sigla
        String siglaEstado = dto.getNomeEstado().trim().toUpperCase();
        Estados estado = estadosRepository.findBySigla(siglaEstado)
                .orElseThrow(() -> new ExcelProcessingException(
                        linha,
                        "Estado",
                        siglaEstado,
                        "Sigla do estado não encontrada"
                ));
        dto.setIdEstado(estado.getId());

        // Buscar ID da Cidade pelo Nome e Estado ID
        String nomeCidade = dto.getNomeCidade().trim();
        Cidades cidade = cidadesRepository.findByNomeIgnoreCaseAndEstadoId(nomeCidade, estado.getId())
                .orElseThrow(() -> new ExcelProcessingException(
                        linha,
                        "Cidade",
                        nomeCidade,
                        "Cidade não encontrada no estado " + siglaEstado
                ));
        dto.setIdCidade(cidade.getId());

        // Empresas vinculadas (colunas 8 e 9)
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

        return dto;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // Verifica se é uma data (formato do Excel)
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date data = cell.getDateCellValue();
                    return formatarDataParaString(data); // Formata a data para "dd/MM/yyyy"
                } else {
                    // Retorna número sem casas decimais (ex: 16534 vira "16534")
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private String formatarDataParaString(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(data);
    }

    private void validarCamposObrigatorios(TrabalhadorDTO dto, int linha) {
        // Regex para validações
        // Aceita telefones no formato "65 9999-9999" ou "65 99999-9999"
        String regexTelefone = "^\\d{2}\\s\\d{4,5}-\\d{4}$";
        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        // Regex para CPF (aceita tanto formatado quanto sem formatação)
        String regexCPF = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$";
        // Regex para RG ajustado para aceitar tanto o formato com pontuação quanto uma sequência de 10 dígitos
        String regexRG = "^(\\d{10}|\\d{1,2}\\.?\\d{3}\\.?\\d{3}-?\\d{1})$";
        String regexNome = "^[\\p{L} ]+$"; // Apenas letras e espaços

        // Validação do Nome
        if (dto.getNome() == null || dto.getNome().isEmpty()) {
            throw new ExcelProcessingException(linha, "Nome", dto.getNome(), "Campo obrigatório não informado");
        } else if (!dto.getNome().matches(regexNome)) {
            throw new ExcelProcessingException(linha, "Nome", dto.getNome(), "Deve conter apenas letras e espaços");
        }

        // Validação da Cidade
        if (dto.getNomeCidade() == null || dto.getNomeCidade().isEmpty()) {
            throw new ExcelProcessingException(linha, "Cidade", dto.getNomeCidade(), "Campo obrigatório não informado");
        } else if (!dto.getNomeCidade().matches(regexNome)) {
            throw new ExcelProcessingException(linha, "Cidade", dto.getNomeCidade(), "Nome inválido para cidade");
        }

        // Validação do Estado
        if (dto.getNomeEstado() == null || dto.getNomeEstado().isEmpty()) {
            throw new ExcelProcessingException(linha, "Estado", dto.getNomeEstado(), "Campo obrigatório não informado");
        } else if (!dto.getNomeEstado().matches(regexNome)) {
            throw new ExcelProcessingException(linha, "Estado", dto.getNomeEstado(), "Nome inválido para estado");
        }

        // Validação do Telefone
        if (dto.getTelefone() == null || dto.getTelefone().isEmpty()) {
            throw new ExcelProcessingException(linha, "Telefone", dto.getTelefone(), "Campo obrigatório não informado");
        } else if (!dto.getTelefone().matches(regexTelefone)) {
            throw new ExcelProcessingException(linha, "Telefone", dto.getTelefone(),
                    "Formato inválido (use o padrão: XX 9999-9999 ou XX 99999-9999)");
        }

        // Validação do E-mail
        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && !dto.getEmail().matches(regexEmail)) {
            throw new ExcelProcessingException(linha, "E-mail", dto.getEmail(), "Formato inválido (ex: usuario@dominio.com)");
        }

        // Validação do RG
        if (dto.getRg() == null || dto.getRg().isEmpty()) {
            throw new ExcelProcessingException(linha, "RG", dto.getRg(), "Campo obrigatório não informado");
        } else if (!dto.getRg().matches(regexRG)) {
            throw new ExcelProcessingException(linha, "RG", dto.getRg(), "Formato inválido (ex: 1234567890 ou 12.345.678-9)");
        }

        // Sanitiza o CPF antes de validar
        String cpfSanitizado = sanitizarCPF(dto.getCpf());

        // Validação do CPF (obrigatório + formato)
        if (dto.getCpf() == null || dto.getCpf().isEmpty()) {
            throw new ExcelProcessingException(linha, "CPF", dto.getCpf(), "Campo obrigatório não informado");
        } else if (!cpfSanitizado.matches(regexCPF)) {
            throw new ExcelProcessingException(
                    linha,
                    "CPF",
                    dto.getCpf(),
                    "Formato inválido (ex: 123.456.789-00 ou 12345678900)"
            );
        }

        // Validação da Data de Nascimento
        if (dto.getDataNascimento() == null || dto.getDataNascimento().isEmpty()) {
            throw new ExcelProcessingException(linha, "DataNascimento", dto.getDataNascimento(), "Campo obrigatório não informado");
        }
    }


    private String sanitizarCPF(String cpf) {
        return cpf.replaceAll("[^0-9]", ""); // Remove pontos e traço
    }

    private void validarCamposUnicos(TrabalhadorDTO dto, int linha) {
        if (repository.existsByCpf(dto.getCpf())) {
            throw new ExcelProcessingException(linha, "CPF", dto.getCpf(), "Já cadastrado");
        }

        if (repository.existsByRg(dto.getRg())) {
            throw new ExcelProcessingException(linha, "RG", dto.getRg(), "Já cadastrado");
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && repository.existsByEmail(dto.getEmail())) {
            throw new ExcelProcessingException(linha, "E-mail", dto.getEmail(), "Já cadastrado");
        }
    }

    private ExcelProcessingException tratarErroGenerico(Exception ex, int linha) {
        Throwable causaRaiz = getCausaRaiz(ex);

        if (causaRaiz instanceof DateTimeParseException) {
            return new ExcelProcessingException(
                    linha,
                    "DataNascimento",
                    "", // Valor não disponível aqui
                    "Formato inválido (use DD/MM/AAAA)"
            );
        }

        return new ExcelProcessingException(
                linha,
                "Geral",
                "",
                "Erro desconhecido: " + causaRaiz.getMessage()
        );
    }

    private Throwable getCausaRaiz(Throwable ex) {
        return ex.getCause() != null ? getCausaRaiz(ex.getCause()) : ex;
    }
}
