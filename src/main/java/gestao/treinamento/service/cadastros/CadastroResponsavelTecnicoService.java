package gestao.treinamento.service.cadastros;

import gestao.treinamento.model.dto.cadastros.ResponsavelTecnicoDTO;
import gestao.treinamento.model.entidades.ResponsavelTecnico;
import gestao.treinamento.repository.cadastros.CadastroResponsavelTecnicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CadastroResponsavelTecnicoService {

    @Autowired
    private final CadastroResponsavelTecnicoRepository repository;

    // GET: Buscar todos os ResponsavelTecnicos
    public List<ResponsavelTecnicoDTO> consultaCadastro() {
        List<ResponsavelTecnico> responsavelTecnicos = repository.findAll();

        return responsavelTecnicos.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo ResponsavelTecnico
    @Transactional
    public ResponsavelTecnicoDTO criarResponsavelTecnico(ResponsavelTecnicoDTO dto) {
        // Converter o DTO para entidade ResponsavelTecnico
        ResponsavelTecnico responsavelTecnico = convertToEntity(dto);

        // Salvar o ResponsavelTecnico e obter o ID gerado
        responsavelTecnico = repository.save(responsavelTecnico);

        // Retornar o DTO do ResponsavelTecnico criado
        return convertToDTO(responsavelTecnico);
    }


    // PUT: Atualizar ResponsavelTecnico existente
    @Transactional
    public ResponsavelTecnicoDTO atualizarResponsavelTecnico(Long id, ResponsavelTecnicoDTO dto) {
        ResponsavelTecnico responsavelTecnicoExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ResponsavelTecnico com ID " + id + " não encontrado"));

        responsavelTecnicoExistente.setNome(dto.getNome());
        responsavelTecnicoExistente.setCpf(dto.getCpf());
        responsavelTecnicoExistente.setNumeroConselho(dto.getNumeroConselho());

        ResponsavelTecnico responsavelTecnicoAtualizado = repository.save(responsavelTecnicoExistente);
        return convertToDTO(responsavelTecnicoAtualizado);
    }

    // DELETE: Excluir ResponsavelTecnico por ID
    public void deletarResponsavelTecnico(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ResponsavelTecnico com ID " + id + " não encontrado");
        }
        repository.deleteById(id);
    }

    // DELETE: Excluir múltiplos ResponsavelTecnicos por lista de IDs
    public void deletarResponsavelTecnicos(List<Long> ids) {
        List<ResponsavelTecnico> responsavelTecnicos = repository.findAllById(ids);

        if (responsavelTecnicos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum ResponsavelTecnico encontrado para os IDs fornecidos");
        }

        repository.deleteAll(responsavelTecnicos);
    }

    // Método auxiliar: Converter entidade para DTO
    private ResponsavelTecnicoDTO convertToDTO(ResponsavelTecnico responsavelTecnico) {
        ResponsavelTecnicoDTO dto = new ResponsavelTecnicoDTO();

        dto.setId(responsavelTecnico.getId());
        dto.setNome(responsavelTecnico.getNome());
        dto.setCpf(responsavelTecnico.getCpf());
        dto.setNumeroConselho(responsavelTecnico.getNumeroConselho());

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private ResponsavelTecnico convertToEntity(ResponsavelTecnicoDTO dto) {
        ResponsavelTecnico responsavelTecnico = new ResponsavelTecnico();
        responsavelTecnico.setNome(dto.getNome());
        responsavelTecnico.setCpf(dto.getCpf());
        responsavelTecnico.setNumeroConselho(dto.getNumeroConselho());
        return responsavelTecnico;
    }
}