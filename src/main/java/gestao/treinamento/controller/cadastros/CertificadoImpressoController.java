package gestao.treinamento.controller.cadastros;

import gestao.treinamento.model.dto.cadastros.CertificadoImpressoDTO;
import gestao.treinamento.service.cadastros.CadastroCertificadoImpressoService;
import gestao.treinamento.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cadastros/certificados")
@AllArgsConstructor
@CrossOrigin
public class CertificadoImpressoController {

    private final CadastroCertificadoImpressoService serviceCertificadoImpresso;

    @PostMapping("/dadosCertificado")
    public ResponseEntity<ApiResponse<CertificadoImpressoDTO>> criarDadosCertificado(@RequestBody @Valid CertificadoImpressoDTO dto) {
        CertificadoImpressoDTO certificadoCriado = serviceCertificadoImpresso.criarCertificado(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Dados do certificado criados com sucesso", certificadoCriado));
    }

    @PutMapping(value = "/arquivoCertificado/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CertificadoImpressoDTO>> atualizarArquivoCertificado(
            @PathVariable Long id,
            @RequestPart("arquivo") MultipartFile arquivo) {
        CertificadoImpressoDTO certificadoAtualizado = serviceCertificadoImpresso.atualizarArquivoCertificado(id, arquivo);
        return ResponseEntity.ok(new ApiResponse<>(true, "Arquivo do certificado atualizado com sucesso", certificadoAtualizado));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CertificadoImpressoDTO>>> getCertificados() {
        List<CertificadoImpressoDTO> certificados = serviceCertificadoImpresso.consultaCertificados();
        return ResponseEntity.ok(new ApiResponse<>(true, "Certificados recuperados com sucesso", certificados));
    }
}
