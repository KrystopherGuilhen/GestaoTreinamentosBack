package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.DuplicateException;
import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.ResponsavelTecnicoAssinaturaDTO;
import gestao.treinamento.model.dto.cadastros.ResponsavelTecnicoDTO;
import gestao.treinamento.model.entidades.ResponsavelTecnico;
import gestao.treinamento.model.entidades.ResponsavelTecnicoAssinatura;
import gestao.treinamento.repository.cadastros.CadastroResponsavelTecnicoAssinaturaRepository;
import gestao.treinamento.repository.cadastros.CadastroResponsavelTecnicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CadastroResponsavelTecnicoService {

    @Autowired
    private final CadastroResponsavelTecnicoRepository repository;
    private final CadastroResponsavelTecnicoAssinaturaRepository responsavelTecnicoAssinaturaRepository;

    // GET: Buscar todos os ResponsavelTecnicos
    public List<ResponsavelTecnicoDTO> consultaCadastro() {
        List<ResponsavelTecnico> responsavelTecnicos = repository.findAll();

        return responsavelTecnicos.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo ResponsavelTecnico
    @Transactional
    public ResponsavelTecnicoDTO criarResponsavelTecnico(ResponsavelTecnicoDTO dto) {
        // Validação de CPF único
        if (repository.existsByCpf(dto.getCpf())) {
            throw new DuplicateException("O CPF " + dto.getCpf() + " já existe nos registros.");
        }

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
        ResponsavelTecnico existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ResponsavelTecnico com ID " + id + " não encontrado"));

        if (!existente.getCpf().equals(dto.getCpf())) {
            if (repository.existsByCpf(dto.getCpf())) {
                throw new DuplicateException("O CPF " + dto.getCpf() + " já existe nos registros.");
            }
        }

        existente.setNome(dto.getNome());
        existente.setCpf(dto.getCpf());
        existente.setNumeroConselho(dto.getNumeroConselho());

        // 1. Atualizar assinatura (estratégia de substituição completa)
        if (dto.getAssinatura() != null) {
            // Remover assinatura existentes
            responsavelTecnicoAssinaturaRepository.deleteByResponsavelTecnicoId(id); // Método customizado no repository

            // Adicionar novas assinaturas
            List<ResponsavelTecnicoAssinatura> novasAssinaturas = dto.getAssinatura().stream()
                    .map(assinaturaDTO -> {
                        ResponsavelTecnicoAssinatura assinatura = new ResponsavelTecnicoAssinatura();
                        assinatura.setName(assinaturaDTO.getName());
                        assinatura.setMimeType(assinaturaDTO.getMimeType());
                        assinatura.setType(assinaturaDTO.getType());
                        assinatura.setSize(assinaturaDTO.getSize());

                        // Decodificar base64
                        String base64Data = assinaturaDTO.getBase64();
                        if (base64Data.contains(",")) {
                            base64Data = base64Data.split(",", 2)[1];
                        }
                        assinatura.setDados(Base64.getDecoder().decode(base64Data));

                        assinatura.setResponsavelTecnico(existente);
                        return assinatura;
                    })
                    .collect(Collectors.toList());

            responsavelTecnicoAssinaturaRepository.saveAll(novasAssinaturas);
        }

        ResponsavelTecnico responsavelTecnicoAtualizado = repository.save(existente);
        return convertToDTO(responsavelTecnicoAtualizado);
    }

    // DELETE: Excluir ResponsavelTecnico por ID
    public void deletarResponsavelTecnico(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Responsável Técnico com ID " + id + " não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Responsável Técnico não pode ser excluído pois está vinculado a outro cadastro");
        }
    }

    // DELETE: Excluir múltiplos ResponsavelTecnicos
    public void deletarResponsavelTecnicos(List<Long> ids) {
        List<ResponsavelTecnico> responsavelTecnicos = repository.findAllById(ids);
        if (responsavelTecnicos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum responsável técnico encontrado para os IDs fornecidos");
        }
        try {
            repository.deleteAll(responsavelTecnicos);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Um ou mais responsáveis técnicos não podem ser excluídos pois estão vinculados a outros cadastros.");
        }
    }

    // Método auxiliar: Converter entidade para DTO
    private ResponsavelTecnicoDTO convertToDTO(ResponsavelTecnico responsavelTecnico) {
        ResponsavelTecnicoDTO dto = new ResponsavelTecnicoDTO();

        dto.setId(responsavelTecnico.getId());
        dto.setNome(responsavelTecnico.getNome());
        dto.setCpf(responsavelTecnico.getCpf());
        dto.setNumeroConselho(responsavelTecnico.getNumeroConselho());

        // Converter assinatura para DTOs
        if (responsavelTecnico.getAssinatura() != null) {
            List<ResponsavelTecnicoAssinaturaDTO> assinaturaDTO = responsavelTecnico.getAssinatura().stream()
                    .map(assinatura -> {
                        ResponsavelTecnicoAssinaturaDTO assDTO = new ResponsavelTecnicoAssinaturaDTO();

                        // Mapear campos adicionais
                        assDTO.setName(assinatura.getName());
                        assDTO.setMimeType(assinatura.getMimeType());
                        assDTO.setType(assinatura.getType());
                        assDTO.setSize(assinatura.getSize());

                        // Apenas o Base64, sem o prefixo "data:"
                        assDTO.setBase64(Base64.getEncoder().encodeToString(assinatura.getDados()));

                        // ObjectURL geralmente não é persistido, mas se necessário:
                        assDTO.setObjectURL(assinatura.getObjectURL());

                        return assDTO;
                    })
                    .collect(Collectors.toList());
            dto.setAssinatura(assinaturaDTO);
        }

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private ResponsavelTecnico convertToEntity(ResponsavelTecnicoDTO dto) {
        ResponsavelTecnico responsavelTecnico = new ResponsavelTecnico();
        responsavelTecnico.setNome(dto.getNome());
        responsavelTecnico.setCpf(dto.getCpf());
        responsavelTecnico.setNumeroConselho(dto.getNumeroConselho());

        // Processar assinatura (DTO → Entidade)
        if (dto.getAssinatura() != null) {
            List<ResponsavelTecnicoAssinatura> assinaturaEntidade = dto.getAssinatura().stream()
                    .map(assinaturaDTO -> {
                        ResponsavelTecnicoAssinatura assinatura = new ResponsavelTecnicoAssinatura();

                        // Mapear campos diretos
                        assinatura.setName(assinaturaDTO.getName());
                        assinatura.setMimeType(assinaturaDTO.getMimeType());
                        assinatura.setType(assinaturaDTO.getType());
                        assinatura.setSize(assinaturaDTO.getSize());

                        // Decodificar Base64 (já está pronto para byte[])
                        assinatura.setDados(Base64.getDecoder().decode(assinaturaDTO.getBase64()));

                        assinatura.setResponsavelTecnico(responsavelTecnico);
                        return assinatura;
                    })
                    .collect(Collectors.toList());
            responsavelTecnico.setAssinatura(assinaturaEntidade);
        }

        return responsavelTecnico;
    }
}