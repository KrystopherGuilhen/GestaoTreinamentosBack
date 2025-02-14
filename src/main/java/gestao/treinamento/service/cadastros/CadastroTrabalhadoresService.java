package gestao.treinamento.service.cadastros;

import gestao.treinamento.model.dto.cadastros.TrabalhadorDTO;
import gestao.treinamento.model.entidades.Empresa;
import gestao.treinamento.model.entidades.Trabalhador;
import gestao.treinamento.model.entidades.TrabalhadorEmpresa;
import gestao.treinamento.repository.cadastros.CadastroEmpresasRepository;
import gestao.treinamento.repository.cadastros.CadastroTrabalhadorEmpresaRepository;
import gestao.treinamento.repository.cadastros.CadastroTrabalhadoresRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class CadastroTrabalhadoresService {

    @Autowired
    private final CadastroTrabalhadoresRepository repository;
    private final CadastroEmpresasRepository empresaRepository;
    private final CadastroTrabalhadorEmpresaRepository trabalhadorEmpresaRepository;

    // GET: Buscar todos os trabalhadores
    public List<TrabalhadorDTO> consultaCadastro() {
        List<Trabalhador> trabalhadores = repository.findAll();

        return trabalhadores.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo trabalhador
    @Transactional
    public TrabalhadorDTO criarTrabalhador(TrabalhadorDTO dto) {
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


    // PUT: Atualizar trabalhador existente
    @Transactional
    public TrabalhadorDTO atualizarTrabalhador(Long id, TrabalhadorDTO dto) {
        Trabalhador existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trabalhador com ID " + id + " não encontrado"));

        existente.setNome(dto.getNome());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setTelefone(dto.getTelefone());
        existente.setCpf(dto.getCpf());
        existente.setRg(dto.getRg());

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

        existente.setEmail(dto.getEmail());

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
        repository.deleteById(id);
    }

    // DELETE: Excluir múltiplos trabalhadores por lista de IDs
    public void excluirTrabalhadores(List<Long> ids) {
        List<Trabalhador> trabalhadores = repository.findAllById(ids);

        if (trabalhadores.isEmpty()) {
            throw new EntityNotFoundException("Nenhum trabalhador encontrado para os IDs fornecidos");
        }

        repository.deleteAll(trabalhadores);
    }

    // Método auxiliar: Converter entidade para DTO
    private TrabalhadorDTO convertToDTO(Trabalhador trabalhador) {
        TrabalhadorDTO dto = new TrabalhadorDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dto.setId(trabalhador.getId());
        dto.setNome(trabalhador.getNome());
        dto.setCidade(trabalhador.getCidade());
        dto.setEstado(trabalhador.getEstado());
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
        trabalhador.setCidade(dto.getCidade());
        trabalhador.setEstado(dto.getEstado());
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
}
