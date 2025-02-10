package gestao.treinamento.service.cadastros;

import gestao.treinamento.exception.ResourceNotFoundException;
import gestao.treinamento.model.dto.cadastros.EmpresaDTO;
import gestao.treinamento.model.entidades.Empresa;
import gestao.treinamento.model.entidades.EmpresaIndustria;
import gestao.treinamento.model.entidades.Industria;
import gestao.treinamento.repository.cadastros.CadastroEmpresaIndustriaRepository;
import gestao.treinamento.repository.cadastros.CadastroEmpresasRepository;
import gestao.treinamento.repository.cadastros.CadastroIndustriasRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CadastroEmpresasService {

    @Autowired
    private final CadastroEmpresasRepository repository;

    private final CadastroIndustriasRepository industriasRepository;
    private final CadastroEmpresaIndustriaRepository empresaIndustriaRepository;

    public List<EmpresaDTO> consultaCadastro() {
        List<Empresa> cursos = repository.findAll();

        return cursos.stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public EmpresaDTO criarEmpresa(EmpresaDTO dto) {
        // Converter o DTO para entidade Curso
        Empresa empresa = convertToEntity(dto);

        // Salvar a Empresa e obter o ID gerado
        empresa = repository.save(empresa);

        // Verificar se há uma industria vinculado no DTO
        if (dto.getIdIndustriaVinculo() != null) {
            // Recuperar a indsutria pelo ID
            Industria industria = industriasRepository.findById(dto.getIdIndustriaVinculo())
                    .orElseThrow(() -> new RuntimeException("Industria não encontrado: ID " + dto.getIdIndustriaVinculo()));

            // Criar a associação empresa-industria
            EmpresaIndustria empresaIndustria = new EmpresaIndustria();
            empresaIndustria.setEmpresa(empresa);
            empresaIndustria.setIndustria(industria);

            // Salvar a associação
            empresaIndustriaRepository.save(empresaIndustria);
        }

        // Retornar o DTO da Empresa criado
        return convertToDTO(empresa);
    }

    public EmpresaDTO atualizarEmpresa(Long id, EmpresaDTO dto) {
        Empresa existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrado com ID: " + id));

        existente.setNome(dto.getNome());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setTelefone(dto.getTelefone());
        existente.setCnpj(dto.getCnpj());
        existente.setCpf(dto.getCpf());
        existente.setNomeResponsavelEmpresa(dto.getNomeResponsavelEmpresa());
        existente.setEmailResponsavelEmpresa(dto.getEmailResponsavelEmpresa());
        existente.setRelacaoEspacoConfinado(dto.getRelacaoEspacoConfinado());

        // Atualizar associações com industria
        if (dto.getIdIndustriaVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idEmpresa e idIndustria)
            List<Long> idsIndustriasVinculadas = empresaIndustriaRepository.findIndustriaByEmpresaId(id);

            // Verificar se o modalidade atual está na lista de associações
            Long idIndustriaVinculo = dto.getIdIndustriaVinculo();

            // Remover associações que não correspondem ao novo modalidade vinculado
            List<Long> idsParaRemover = idsIndustriasVinculadas.stream()
                    .filter(idModalidade -> !idModalidade.equals(idIndustriaVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                empresaIndustriaRepository.deleteByEmpresaIdAndIndustriaIds(id, idsParaRemover);
            }

            // Verificar se a industria já está associado
            boolean existe = empresaIndustriaRepository.existsByEmpresaIdAndIndustriaId(id, idIndustriaVinculo);
            if (!existe) {
                // Recuperar a industria pelo ID
                Industria industria = industriasRepository.findById(idIndustriaVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Modalidade com ID " + idIndustriaVinculo + " não encontrado"));

                // Criar a nova associação
                EmpresaIndustria novaAssociacao = new EmpresaIndustria();
                novaAssociacao.setEmpresa(existente);
                novaAssociacao.setIndustria(industria);

                // Salvar a nova associação
                empresaIndustriaRepository.save(novaAssociacao);
            }
        }

        Empresa empresaAtualizada = repository.save(existente);
        return convertToDTO(empresaAtualizada);
    }


    public void deletarEmpresa(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa não encontrada com ID: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("A empresa não pode ser excluída pois está vinculada a outro cadastro.");
        }
    }

    public void deletarEmpresas(List<Long> ids) {
        List<Empresa> empresas = repository.findAllById(ids);
        if (empresas.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais IDs não foram encontrados.");
        }
        try {
            repository.deleteAll(empresas);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Uma ou mais empresas não podem ser excluídas pois estão vinculadas a outros cadastros.");
        }
    }

    // Método auxiliar: Converter entidade para DTO
    private EmpresaDTO convertToDTO(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();

        dto.setId(empresa.getId());
        dto.setNome(empresa.getNome());
        dto.setCidade(empresa.getCidade());
        dto.setEstado(empresa.getEstado());
        dto.setTelefone(empresa.getTelefone());
        dto.setCnpj(empresa.getCnpj());
        dto.setCpf(empresa.getCpf());
        dto.setNomeResponsavelEmpresa(empresa.getNomeResponsavelEmpresa());
        dto.setEmailResponsavelEmpresa(empresa.getEmailResponsavelEmpresa());
        dto.setRelacaoEspacoConfinado(empresa.getRelacaoEspacoConfinado());

        // Extrai os IDs e nomes das empresas vinculadas
//        List<Long> idEmpresas = curso.getEmpresasVinculadas() != null
//                ? curso.getEmpresasVinculadas().stream()
//                .map(te -> te.getEmpresa().getId())
//                .toList()
//                : new ArrayList<>();
//        dto.setIdEmpresaVinculo(idEmpresas);
//
//        List<String> nomesEmpresas = curso.getEmpresasVinculadas().stream()
//                .map(te -> te.getEmpresa().getNome())
//                .toList();
//
//        dto.setIdEmpresaVinculo(idEmpresas);
//        dto.setNomeEmpresaVinculo(nomesEmpresas);

        // Extrai o ID e nome da modalidade vinculada (única)
        if (empresa.getIndustriasVinculadas() != null && !empresa.getIndustriasVinculadas().isEmpty()) {
            EmpresaIndustria empresaIndustria = empresa.getIndustriasVinculadas().get(0);
            dto.setIdIndustriaVinculo(empresaIndustria.getIndustria().getId());

            List<String> nomeIndustrias = empresa.getIndustriasVinculadas().stream()
                    .map(te -> te.getIndustria().getNome())
                    .toList();
            dto.setNomeIndustriaVinculo(nomeIndustrias);
        } else {
            dto.setIdIndustriaVinculo(null);
            dto.setNomeIndustriaVinculo(null);
        }

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Empresa convertToEntity(EmpresaDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNome());
        empresa.setCidade(dto.getCidade());
        empresa.setEstado(dto.getEstado());
        empresa.setTelefone(dto.getTelefone());
        empresa.setCnpj(dto.getCnpj());
        empresa.setCpf(dto.getCpf());
        empresa.setNomeResponsavelEmpresa(dto.getNomeResponsavelEmpresa());
        empresa.setEmailResponsavelEmpresa(dto.getEmailResponsavelEmpresa());
        empresa.setRelacaoEspacoConfinado(dto.getRelacaoEspacoConfinado());

        return empresa;
    }
}