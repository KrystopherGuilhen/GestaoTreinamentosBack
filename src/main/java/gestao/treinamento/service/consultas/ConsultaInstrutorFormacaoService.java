package gestao.treinamento.service.consultas;

import gestao.treinamento.model.dto.consultas.InstrutorFormacaoConsultaDTO;
import gestao.treinamento.model.dto.consultas.InstrutorFormacaoConsultaListaDTO;
import gestao.treinamento.repository.consultas.ConsultaInstrutorFormacaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ConsultaInstrutorFormacaoService {


    @Autowired
    private ConsultaInstrutorFormacaoRepository repository;

    public List<InstrutorFormacaoConsultaDTO> buscarFormacoesPorInstrutores(List<Long> instrutorIds) {
        List<Object[]> resultados = repository.findFormacoesAgrupadas(instrutorIds);

        // Agrupa por instrutorId
        Map<Long, List<InstrutorFormacaoConsultaListaDTO>> formacoesPorInstrutor = resultados.stream()
                .collect(Collectors.groupingBy(
                        resultado -> (Long) resultado[0], // instrutorId
                        Collectors.mapping(
                                resultado -> new InstrutorFormacaoConsultaListaDTO(
                                        (Long) resultado[1], // formacaoId
                                        (String) resultado[2] // formacaoNome
                                ),
                                Collectors.toList()
                        )
                ));

        // Converte para List<InstrutorFormacoesDTO>
        return formacoesPorInstrutor.entrySet().stream()
                .map(entry -> new InstrutorFormacaoConsultaDTO(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
    }
}