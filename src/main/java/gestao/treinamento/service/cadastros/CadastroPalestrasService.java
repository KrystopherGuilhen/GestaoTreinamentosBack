package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.PalestraDTO;
import gestao.treinamento.model.entidades.Palestra;
import gestao.treinamento.repository.cadastros.CadastroEmpresasRepository;
import gestao.treinamento.repository.cadastros.CadastroPalestrasRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CadastroPalestrasService {

    @Autowired
    private final CadastroPalestrasRepository repository;

    // GET: Buscar todos os palestras
    public List<PalestraDTO> consultaCadastro() {
        List<Palestra> palestras = repository.findAll();

        return palestras.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo palestra
    @Transactional
    public PalestraDTO criarPalestra(PalestraDTO dto) {
        // Converter o DTO para entidade palestra
        Palestra palestra = convertToEntity(dto);

        // Salvar o palestra e obter o ID gerado
        palestra = repository.save(palestra);

        // Retornar o DTO do palestra criado
        return convertToDTO(palestra);
    }


    // PUT: Atualizar palestra existente
    @Transactional
    public PalestraDTO atualizarPalestra(Long id, PalestraDTO dto) {
        Palestra existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Palestra com ID " + id + " não encontrado"));

        existente.setNomeEvento(dto.getNomeEvento());
        existente.setConteudoProgramatico(dto.getConteudoProgramatico());
        existente.setObservacao(dto.getObservacao());
        existente.setCargaHorariaTotal(dto.getCargaHorariaTotal());

        Palestra palestraAtualizado = repository.save(existente);
        return convertToDTO(palestraAtualizado);
    }

    // DELETE: Excluir palestra por ID
    public void excluirPalestra(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Palestra com ID " + id + " não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Palestra não pode ser excluída pois está vinculada a outro cadastro");
        }
    }

    // DELETE: Excluir múltiplos palestras por lista de IDs
    public void excluirPalestras(List<Long> ids) {
        List<Palestra> palestras = repository.findAllById(ids);

        if (palestras.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma Palestra encontrada para os IDs fornecidos");
        }
        try {
            repository.deleteAll(palestras);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Uma ou mais Palestras não podem ser excluídas pois estão vinculadas a outros cadastros.");
        }
    }

    // Método auxiliar: Converter entidade para DTO
    private PalestraDTO convertToDTO(Palestra palestra) {
        PalestraDTO dto = new PalestraDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dto.setId(palestra.getId());
        dto.setNomeEvento(palestra.getNomeEvento());
        dto.setConteudoProgramatico(palestra.getConteudoProgramatico());
        dto.setObservacao(palestra.getObservacao());
        dto.setCargaHorariaTotal(palestra.getCargaHorariaTotal());

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Palestra convertToEntity(PalestraDTO dto) {
        Palestra palestra = new Palestra();
        palestra.setNomeEvento(dto.getNomeEvento());
        palestra.setConteudoProgramatico(dto.getConteudoProgramatico());
        palestra.setObservacao(dto.getObservacao());
        palestra.setCargaHorariaTotal(dto.getCargaHorariaTotal());


        return palestra;
    }
}