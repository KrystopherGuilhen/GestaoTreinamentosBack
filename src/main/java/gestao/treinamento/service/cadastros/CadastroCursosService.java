package gestao.treinamento.service.cadastros;

import gestao.treinamento.model.dto.cadastros.CursoDTO;
import gestao.treinamento.model.entidades.Curso;
import gestao.treinamento.model.entidades.CursoModalidade;
import gestao.treinamento.model.entidades.Modalidade;
import gestao.treinamento.repository.cadastros.CadastroCursoModalidadeRepository;
import gestao.treinamento.repository.cadastros.CadastroCursosRepository;
import gestao.treinamento.repository.cadastros.CadastroModalidadesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CadastroCursosService {

    @Autowired
    private final CadastroCursosRepository repository;

//    private final CadastroEmpresasRepository empresaRepository;
//    private final CadastroCursoEmpresaRepository cursoEmpresaRepository;

    private final CadastroModalidadesRepository modalidadesRepository;
    private final CadastroCursoModalidadeRepository cursoModalidadeRepository;

    // GET: Buscar todos os Cursos
    public List<CursoDTO> consultaCadastro() {
        List<Curso> cursos = repository.findAll();

        return cursos.stream().map(this::convertToDTO).toList();
    }

    // POST: Criar novo Curso
    @Transactional
    public CursoDTO criarCurso(CursoDTO dto) {
        // Converter o DTO para entidade Curso
        Curso curso = convertToEntity(dto);

        // Salvar o Curso e obter o ID gerado
        curso = repository.save(curso);

        // Verificar se há empresas vinculadas no DTO
//        if (dto.getIdEmpresaVinculo() != null && !dto.getIdEmpresaVinculo().isEmpty()) {
//            for (Long idEmpresa : dto.getIdEmpresaVinculo()) {
//                // Recuperar a empresa pelo ID (se necessário)
//                Empresa empresa = empresaRepository.findById(idEmpresa)
//                        .orElseThrow(() -> new RuntimeException("Empresa não encontrada: ID " + idEmpresa));
//
//                // Criar a associação Curso-empresa
//                CursoEmpresa cursoEmpresas = new CursoEmpresa();
//                cursoEmpresas.setCurso(curso);
//                cursoEmpresas.setEmpresa(empresa);
//
//                // Salvar a associação
//                cursoEmpresaRepository.save(cursoEmpresas);
//            }
//        }

        // Verificar se há um modalidade vinculado no DTO
        if (dto.getIdModalidadeVinculo() != null) {
            // Recuperar o evento pelo ID
            Modalidade modalidade = modalidadesRepository.findById(dto.getIdModalidadeVinculo())
                    .orElseThrow(() -> new RuntimeException("Evento não encontrado: ID " + dto.getIdModalidadeVinculo()));

            // Criar a associação curso-evento
            CursoModalidade cursoModalidade = new CursoModalidade();
            cursoModalidade.setCurso(curso);
            cursoModalidade.setModalidade(modalidade);

            // Salvar a associação
            cursoModalidadeRepository.save(cursoModalidade);
        }

        // Retornar o DTO do Curso criado
        return convertToDTO(curso);
    }

    // PUT: Atualizar Curso existente
    @Transactional
    public CursoDTO atualizarCurso(Long id, CursoDTO dto) {
        Curso existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso com ID " + id + " não encontrado"));

        existente.setNome(dto.getNome());
        existente.setConteudoProgramatico(dto.getConteudoProgramatico());
        existente.setCargaHorariaTotal(dto.getCargaHorariaTotal());
        existente.setCargaHorariaEad(dto.getCargaHorariaEad());
        existente.setCargaHorariaPresencial(dto.getCargaHorariaPresencial());
        existente.setPeriodoValidade(dto.getPeriodoValidade());
        //cursoExistente.setValorContratoCrm(cursoDTO.getValorContratoCrm());

        // Atualizar associações com empresas
//        if (cursoDTO.getIdEmpresaVinculo() != null) {
//            // Recuperar as associações existentes (com a chave composta idCurso e idEmpresa)
//            List<Long> idsEmpresasVinculadas = cursoEmpresaRepository.findEmpresasByCursoId(id);
//
//            // Remover associações que não estão mais na lista
//            List<Long> idsParaRemover = idsEmpresasVinculadas.stream()
//                    .filter(idEmpresa -> !cursoDTO.getIdEmpresaVinculo().contains(idEmpresa))
//                    .toList();
//            cursoEmpresaRepository.deleteByCursoIdAndEmpresaIds(id, idsParaRemover);
//
//            // Adicionar novas associações (para cada empresa que não existe na tabela)
//            for (Long idEmpresa : cursoDTO.getIdEmpresaVinculo()) {
//                boolean existe = cursoEmpresaRepository.existsByCursoIdAndEmpresaId(id, idEmpresa);
//                if (!existe) {
//                    Empresa empresa = empresaRepository.findById(idEmpresa)
//                            .orElseThrow(() -> new EntityNotFoundException("Empresa com ID " + idEmpresa + " não encontrada"));
//
//                    CursoEmpresa novaAssociacao = new CursoEmpresa();
//                    novaAssociacao.setCurso(cursoExistente);
//                    novaAssociacao.setEmpresa(empresa);
//
//                    // Aqui, a chave primária (ID) será gerada automaticamente, já que a tabela tem uma chave composta
//                    cursoEmpresaRepository.save(novaAssociacao);
//                }
//            }
//        }

        // Atualizar associações com modalidade
        if (dto.getIdModalidadeVinculo() != null) {
            // Recuperar as associações existentes (com a chave composta idCurso e idModalidade)
            List<Long> idsModalidadesVinculadas = cursoModalidadeRepository.findModalidadeByCursoId(id);

            // Verificar se o modalidade atual está na lista de associações
            Long idModalidadeVinculo = dto.getIdModalidadeVinculo();

            // Remover associações que não correspondem ao novo modalidade vinculado
            List<Long> idsParaRemover = idsModalidadesVinculadas.stream()
                    .filter(idModalidade -> !idModalidade.equals(idModalidadeVinculo))
                    .toList();
            if (!idsParaRemover.isEmpty()) {
                cursoModalidadeRepository.deleteByCursoIdAndModalidadeIds(id, idsParaRemover);
            }

            // Verificar se o modalidade já está associado
            boolean existe = cursoModalidadeRepository.existsByCursoIdAndModalidadeId(id, idModalidadeVinculo);
            if (!existe) {
                // Recuperar o modalidade pelo ID
                Modalidade modalidade = modalidadesRepository.findById(idModalidadeVinculo)
                        .orElseThrow(() -> new EntityNotFoundException("Modalidade com ID " + idModalidadeVinculo + " não encontrado"));

                // Criar a nova associação
                CursoModalidade novaAssociacao = new CursoModalidade();
                novaAssociacao.setCurso(existente);
                novaAssociacao.setModalidade(modalidade);

                // Salvar a nova associação
                cursoModalidadeRepository.save(novaAssociacao);
            }
        }

        Curso cursoAtualizado = repository.save(existente);
        return convertToDTO(cursoAtualizado);
    }

    // DELETE: Excluir Curso por ID
    public void excluirCurso(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Curso com ID " + id + " não encontrado");
        }
        repository.deleteById(id);
    }

    // DELETE: Excluir múltiplos Cursos por lista de IDs
    public void excluirCursos(List<Long> ids) {
        List<Curso> cursos = repository.findAllById(ids);

        if (cursos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum Curso encontrado para os IDs fornecidos");
        }

        repository.deleteAll(cursos);
    }

    // Método auxiliar: Converter entidade para DTO
    private CursoDTO convertToDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();

        dto.setId(curso.getId());
        dto.setNome(curso.getNome());
        dto.setConteudoProgramatico(curso.getConteudoProgramatico());
        dto.setCargaHorariaTotal(curso.getCargaHorariaTotal());
        dto.setCargaHorariaEad(curso.getCargaHorariaEad());
        dto.setCargaHorariaPresencial(curso.getCargaHorariaPresencial());
        dto.setPeriodoValidade(curso.getPeriodoValidade());
        //dto.setValorContratoCrm(curso.getValorContratoCrm());

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
        if (curso.getModalidadesVinculadas() != null && !curso.getModalidadesVinculadas().isEmpty()) {
            CursoModalidade cursoModalidade = curso.getModalidadesVinculadas().get(0);
            dto.setIdModalidadeVinculo(cursoModalidade.getModalidade().getId());
            dto.setNomeModalidadeVinculo(cursoModalidade.getModalidade().getNome());
        } else {
            dto.setIdModalidadeVinculo(null);
            dto.setNomeModalidadeVinculo(null);
        }

        return dto;
    }

    // Método auxiliar: Converter DTO para entidade
    private Curso convertToEntity(CursoDTO dto) {
        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        curso.setConteudoProgramatico(dto.getConteudoProgramatico());
        curso.setCargaHorariaTotal(dto.getCargaHorariaTotal());
        curso.setCargaHorariaEad(dto.getCargaHorariaEad());
        curso.setCargaHorariaPresencial(dto.getCargaHorariaPresencial());
        curso.setPeriodoValidade(dto.getPeriodoValidade());
        //curso.setValorContratoCrm(dto.getValorContratoCrm());

        return curso;
    }
}
