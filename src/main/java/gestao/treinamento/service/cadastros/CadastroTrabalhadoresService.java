package gestao.treinamento.service.cadastros;

import gestao.treinamento.model.dto.TrabalhadorDTO;
import gestao.treinamento.model.entidade.Empresa;
import gestao.treinamento.model.entidade.Trabalhador;
import gestao.treinamento.model.entidade.TrabalhadorEmpresa;
import gestao.treinamento.repository.cadastros.CadastroEmpresasRepository;
import gestao.treinamento.repository.cadastros.CadastroTrabalhadorEmpresaRepository;
import gestao.treinamento.repository.cadastros.CadastroTrabalhadoresRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

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
    public TrabalhadorDTO atualizarTrabalhador(Long id, TrabalhadorDTO trabalhadorDTO) {
        Trabalhador trabalhadorExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trabalhador com ID " + id + " não encontrado"));

        trabalhadorExistente.setNome(trabalhadorDTO.getNome());
        trabalhadorExistente.setCidade(trabalhadorDTO.getCidade());
        trabalhadorExistente.setEstado(trabalhadorDTO.getEstado());
        trabalhadorExistente.setTelefone(trabalhadorDTO.getTelefone());
        trabalhadorExistente.setCpf(trabalhadorDTO.getCpf());
        trabalhadorExistente.setRg(trabalhadorDTO.getRg());

        if (trabalhadorDTO.getDataNascimento() != null) {
            String dataNascimentoStr = trabalhadorDTO.getDataNascimento();
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

            trabalhadorExistente.setDataNascimento(dataNascimento);
        }

        trabalhadorExistente.setEmail(trabalhadorDTO.getEmail());

        // Atualizar associações com empresas
        if (trabalhadorDTO.getIdEmpresaVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idTrabalhador e idEmpresa)
            List<Long> idsEmpresasVinculadas = trabalhadorEmpresaRepository.findEmpresasByTrabalhadorId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsEmpresasVinculadas.stream()
                    .filter(idEmpresa -> !trabalhadorDTO.getIdEmpresaVinculo().contains(idEmpresa))
                    .toList();
            trabalhadorEmpresaRepository.deleteByTrabalhadorIdAndEmpresaIds(id, idsParaRemover);

            // Adicionar novas associações (para cada empresa que não existe na tabela)
            for (Long idEmpresa : trabalhadorDTO.getIdEmpresaVinculo()) {
                boolean existe = trabalhadorEmpresaRepository.existsByTrabalhadorIdAndEmpresaId(id, idEmpresa);
                if (!existe) {
                    Empresa empresa = empresaRepository.findById(idEmpresa)
                            .orElseThrow(() -> new EntityNotFoundException("Empresa com ID " + idEmpresa + " não encontrada"));

                    TrabalhadorEmpresa novaAssociacao = new TrabalhadorEmpresa();
                    novaAssociacao.setTrabalhador(trabalhadorExistente);
                    novaAssociacao.setEmpresa(empresa);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    trabalhadorEmpresaRepository.save(novaAssociacao);
                }
            }
        }

        Trabalhador trabalhadorAtualizado = repository.save(trabalhadorExistente);
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

        // Converter o formato ISO 8601 para LocalDate
        if (dto.getDataNascimento() != null) {
            LocalDate dataNascimento = LocalDate.parse(dto.getDataNascimento().split("T")[0]);
            trabalhador.setDataNascimento(dataNascimento);
        }

        trabalhador.setEmail(dto.getEmail());

        return trabalhador;
    }
}
