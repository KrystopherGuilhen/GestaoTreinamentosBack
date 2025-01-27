package gestao.treinamento.service.cadastros;

import gestao.treinamento.model.dto.PalestraDTO;
import gestao.treinamento.model.entidade.Palestra;
import gestao.treinamento.model.entidade.PalestraEmpresa;
import gestao.treinamento.model.entidade.Empresa;
import gestao.treinamento.repository.cadastros.CadastroPalestraEmpresaRepository;
import gestao.treinamento.repository.cadastros.CadastroPalestrasRepository;
import gestao.treinamento.repository.cadastros.CadastroEmpresasRepository;
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
public class CadastroPalestrasService {

    @Autowired
    private final CadastroPalestrasRepository repository;
    private final CadastroEmpresasRepository empresaRepository;
    private final CadastroPalestraEmpresaRepository palestraEmpresaRepository;

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

        // Verificar se há empresas vinculadas no DTO
        if (dto.getIdEmpresaVinculo() != null && !dto.getIdEmpresaVinculo().isEmpty()) {
            for (Long idEmpresa : dto.getIdEmpresaVinculo()) { 
                // Recuperar a empresa pelo ID (se necessário)
                Empresa empresa = empresaRepository.findById(idEmpresa)
                        .orElseThrow(() -> new RuntimeException("Empresa não encontrada: ID " + idEmpresa));

                // Criar a associação palestra-empresa
                PalestraEmpresa palestraEmpresas = new PalestraEmpresa();
                palestraEmpresas.setPalestra(palestra);
                palestraEmpresas.setEmpresa(empresa);

                // Salvar a associação
                palestraEmpresaRepository.save(palestraEmpresas);
            }
        }

        // Retornar o DTO do palestra criado
        return convertToDTO(palestra);
    }


    // PUT: Atualizar palestra existente
    @Transactional
    public PalestraDTO atualizarPalestra(Long id, PalestraDTO palestraDTO) {
        Palestra palestraExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Palestra com ID " + id + " não encontrado"));

        palestraExistente.setNomeEvento(palestraDTO.getNomeEvento());
        palestraExistente.setCidadeEvento(palestraDTO.getCidadeEvento());

        if (palestraDTO.getDataInicio() != null) {
            String dataInicioStr = palestraDTO.getDataInicio();
            LocalDate dataInicio = null;

            try {
                // Primeiro, tenta converter do formato ISO 8601
                if (dataInicioStr.contains("T")) {
                    dataInicio = LocalDate.parse(dataInicioStr.split("T")[0]);
                } else {
                    // Tenta converter do formato dd/MM/yyyy
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dataInicio = LocalDate.parse(dataInicioStr, formatter);
                }
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de data inválido: " + dataInicioStr);
            }

            palestraExistente.setDataInicio(dataInicio);
        }

        if (palestraDTO.getDataFim() != null) {
            String dataFimStr = palestraDTO.getDataFim();
            LocalDate dataFim = null;

            try {
                // Primeiro, tenta converter do formato ISO 8601
                if (dataFimStr.contains("T")) {
                    dataFim = LocalDate.parse(dataFimStr.split("T")[0]);
                } else {
                    // Tenta converter do formato dd/MM/yyyy
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dataFim = LocalDate.parse(dataFimStr, formatter);
                }
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de data inválido: " + dataFimStr);
            }

            palestraExistente.setDataFim(dataFim);
        }

        palestraExistente.setValorContratoCrm(palestraDTO.getValorContratoCrm());
        palestraExistente.setNumeroContratoCrm(palestraDTO.getNumeroContratoCrm());

        // Atualizar associações com empresas
        if (palestraDTO.getIdEmpresaVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idPalestra e idEmpresa)
            List<Long> idsEmpresasVinculadas = palestraEmpresaRepository.findEmpresasByPalestraId(id);

            // Remover associações que não estão mais na lista
            List<Long> idsParaRemover = idsEmpresasVinculadas.stream()
                    .filter(idEmpresa -> !palestraDTO.getIdEmpresaVinculo().contains(idEmpresa))
                    .toList();
            palestraEmpresaRepository.deleteByPalestraIdAndEmpresaIds(id, idsParaRemover);

            // Adicionar novas associações (para cada empresa que não existe na tabela)
            for (Long idEmpresa : palestraDTO.getIdEmpresaVinculo()) {
                boolean existe = palestraEmpresaRepository.existsByPalestraIdAndEmpresaId(id, idEmpresa);
                if (!existe) {
                    Empresa empresa = empresaRepository.findById(idEmpresa)
                            .orElseThrow(() -> new EntityNotFoundException("Empresa com ID " + idEmpresa + " não encontrada"));

                    PalestraEmpresa novaAssociacao = new PalestraEmpresa();
                    novaAssociacao.setPalestra(palestraExistente);
                    novaAssociacao.setEmpresa(empresa);

                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
                    palestraEmpresaRepository.save(novaAssociacao);
                }
            }
        }

        Palestra palestraAtualizado = repository.save(palestraExistente);
        return convertToDTO(palestraAtualizado);
    }

    // DELETE: Excluir palestra por ID
    public void excluirPalestra(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Palestra com ID " + id + " não encontrado");
        }
        repository.deleteById(id);
    }

    // DELETE: Excluir múltiplos palestras por lista de IDs
    public void excluirPalestras(List<Long> ids) {
        List<Palestra> palestras = repository.findAllById(ids);

        if (palestras.isEmpty()) {
            throw new EntityNotFoundException("Nenhum Palestra encontrado para os IDs fornecidos");
        }

        repository.deleteAll(palestras);
    }

    // Método auxiliar: Converter entidade para DTO
    private PalestraDTO convertToDTO(Palestra palestra) {
        PalestraDTO dto = new PalestraDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dto.setId(palestra.getId());
        dto.setNomeEvento(palestra.getNomeEvento());
        dto.setCidadeEvento(palestra.getCidadeEvento());
        dto.setDataInicio(
                palestra.getDataInicio() != null
                        ? palestra.getDataInicio().format(formatter)
                        : null
        );
        dto.setDataFim(
                palestra.getDataFim() != null
                        ? palestra.getDataFim().format(formatter)
                        : null
        );
        dto.setValorContratoCrm(palestra.getValorContratoCrm());
        dto.setValorContratoCrm(palestra.getValorContratoCrm());
        dto.setNumeroContratoCrm(palestra.getNumeroContratoCrm());

        // Extrai os IDs e nomes das empresas vinculadas
        List<Long> idEmpresas = palestra.getEmpresasVinculadas() != null
                ? palestra.getEmpresasVinculadas().stream()
                .map(te -> te.getEmpresa().getId())
                .toList()
                : new ArrayList<>();
        dto.setIdEmpresaVinculo(idEmpresas);

        List<String> nomesEmpresas = palestra.getEmpresasVinculadas().stream()
                .map(te -> te.getEmpresa().getNome())
                .toList();

        dto.setIdEmpresaVinculo(idEmpresas);
        dto.setNomeEmpresaVinculo(nomesEmpresas);

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Palestra convertToEntity(PalestraDTO dto) {
        Palestra palestra = new Palestra();
        palestra.setNomeEvento(dto.getNomeEvento());
        palestra.setCidadeEvento(dto.getCidadeEvento());

        // Converter o formato ISO 8601 para LocalDate
        if (dto.getDataInicio() != null) {
            LocalDate dataNascimento = LocalDate.parse(dto.getDataInicio().split("T")[0]);
            palestra.setDataInicio(dataNascimento);
        }

        // Converter o formato ISO 8601 para LocalDate
        if (dto.getDataFim() != null) {
            LocalDate dataNascimento = LocalDate.parse(dto.getDataFim().split("T")[0]);
            palestra.setDataFim(dataNascimento);
        }

        palestra.setValorContratoCrm(dto.getValorContratoCrm());
        palestra.setNumeroContratoCrm(dto.getNumeroContratoCrm());

        return palestra;
    }
}