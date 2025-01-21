package gestao.treinamento.service;

import gestao.treinamento.model.dto.TrabalhadorDTO;
import gestao.treinamento.model.entidade.Trabalhador;
import gestao.treinamento.repository.CadastroTrabalhadoresRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CadastroTrabalhadoresService {

    @Autowired
    private final CadastroTrabalhadoresRepository repository;

    public List<TrabalhadorDTO> consultaCadastro() {
        List<Trabalhador> trabalhadores = repository.findAll();

        return trabalhadores.stream().map(this::convertToDTO).toList();
    }

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
        // Formata a data no formato brasileiro antes de definir no DTO
        dto.setDataNascimento(
                trabalhador.getDataNascimento() != null
                        ? trabalhador.getDataNascimento().format(formatter)
                        : null
        );
        dto.setEmail(trabalhador.getEmail());

        // Extrai os IDs e nomes das empresas vinculadas
        List<Long> idEmpresas = trabalhador.getEmpresasVinculadas().stream()
                .map(te -> te.getEmpresa().getId())
                .toList();

        List<String> nomesEmpresas = trabalhador.getEmpresasVinculadas().stream()
                .map(te -> te.getEmpresa().getNome())
                .toList();

        dto.setIdEmpresaVinculo(idEmpresas);
        dto.setNomeEmpresaVinculo(nomesEmpresas);

        return dto;
    }
}