package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.CertificadoImpressoDTO;
import gestao.treinamento.model.entidades.CertificadoImpresso;
import gestao.treinamento.repository.cadastros.CadastroCertificadoImpressoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CadastroCertificadoImpressoService {

    @Autowired
    private final CadastroCertificadoImpressoRepository repository;

    // Consulta todos os certificados
    public List<CertificadoImpressoDTO> consultaCertificados() {
        List<CertificadoImpresso> certificados = repository.findAll();
        return certificados.stream().map(this::convertToDTO).toList();
    }

    // Cria os dados do certificado (POST) – sem arquivo PDF
    @Transactional
    public CertificadoImpressoDTO criarCertificado(CertificadoImpressoDTO dto) {
        CertificadoImpresso certificado = convertToEntity(dto);
        // Neste método, o campo "arquivo" pode permanecer nulo
        certificado = repository.save(certificado);
        return convertToDTO(certificado);
    }

    // Atualiza o registro com o arquivo PDF (PUT)
    @Transactional
    public CertificadoImpressoDTO atualizarArquivoCertificado(Long id, MultipartFile arquivo) {
        CertificadoImpresso certificado = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificado não encontrado com ID: " + id));
        try {
            // Converte o arquivo PDF para array de bytes e atualiza o registro
            certificado.setArquivo(arquivo.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo PDF", e);
        }
        certificado = repository.save(certificado);
        return convertToDTO(certificado);
    }

//    // Remove um certificado pelo ID
//    public void deletarCertificado(Long id) {
//        if (!repository.existsById(id)) {
//            throw new ResourceNotFoundException("Certificado não encontrado com ID: " + id);
//        }
//        try {
//            repository.deleteById(id);
//        } catch (DataIntegrityViolationException e) {
//            throw new IllegalStateException("O certificado não pode ser excluído pois está vinculado a outro cadastro.");
//        }
//    }
//
//    // Remove múltiplos certificados a partir de uma lista de IDs
//    public void deletarCertificados(List<Long> ids) {
//        List<CertificadoImpresso> certificados = repository.findAllById(ids);
//        if (certificados.size() != ids.size()) {
//            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
//        }
//        try {
//            repository.deleteAll(certificados);
//        } catch (DataIntegrityViolationException e) {
//            throw new IllegalStateException("Um ou mais certificados não podem ser excluídos pois estão vinculados a outros cadastros.");
//        }
//    }

    // Método auxiliar para converter entidade para DTO
    private CertificadoImpressoDTO convertToDTO(CertificadoImpresso certificado) {
        CertificadoImpressoDTO dto = new CertificadoImpressoDTO();
        dto.setId(certificado.getId());
        if (certificado.getArquivo() != null && certificado.getArquivo().length > 0) {
            dto.setArquivo(certificado.getArquivo());
        }
        dto.setIdTurma(certificado.getIdTurma());
        dto.setNomeTurma(certificado.getNomeTurma());
        dto.setIdEvento(certificado.getIdEvento());
        dto.setNomeEvento(certificado.getNomeEvento());
        dto.setNomeTrabalhador(certificado.getNomeTrabalhador());
        dto.setCpfTrabalhador(certificado.getCpfTrabalhador());
        dto.setRgTrabalhador(certificado.getRgTrabalhador());
        dto.setNomeCurso(certificado.getNomeCurso());
        dto.setModalidadeCurso(certificado.getModalidade());
        dto.setDataInicio(certificado.getDataInicio());
        dto.setDataFim(certificado.getDataFim());
        dto.setDataExpiracao(certificado.getDataExpiracao());
        dto.setNomeCidadeTreinamento(certificado.getNomeCidadeTreinamento());
        dto.setDataEmissao(certificado.getDataEmissao());
        dto.setNomePalestra(certificado.getNomePalestra());
        dto.setUnidadePolo(certificado.getUnidadePolo());
        dto.setNomeInstrutor(certificado.getNomeInstrutor());
        return dto;
    }

    // Método auxiliar para converter DTO para entidade
    private CertificadoImpresso convertToEntity(CertificadoImpressoDTO dto) {
        CertificadoImpresso certificado = new CertificadoImpresso();
        certificado.setIdTurma(dto.getIdTurma());
        certificado.setNomeTurma(dto.getNomeTurma());
        certificado.setIdEvento(dto.getIdEvento());
        certificado.setNomeEvento(dto.getNomeEvento());
        certificado.setNomeTrabalhador(dto.getNomeTrabalhador());
        certificado.setCpfTrabalhador(dto.getCpfTrabalhador());
        certificado.setRgTrabalhador(dto.getRgTrabalhador());
        certificado.setNomeCurso(dto.getNomeCurso());
        certificado.setModalidade(dto.getModalidadeCurso());
        certificado.setDataInicio(dto.getDataInicio());
        certificado.setDataFim(dto.getDataFim());
        certificado.setDataExpiracao(dto.getDataExpiracao());
        certificado.setNomeCidadeTreinamento(dto.getNomeCidadeTreinamento());
        certificado.setDataEmissao(dto.getDataEmissao());
        certificado.setNomePalestra(dto.getNomePalestra());
        certificado.setUnidadePolo(dto.getUnidadePolo());
        certificado.setNomeInstrutor(dto.getNomeInstrutor());
        return certificado;
    }
}