package gestao.treinamento.repository.consultas;

import gestao.treinamento.model.dto.consultas.InstrutorConsultaDTO;
import gestao.treinamento.model.entidades.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaInstrutoresRepository extends JpaRepository<Instrutor, Long> {

    @Query("SELECT new gestao.treinamento.model.dto.consultas.InstrutorConsultaDTO(i.id, i.nome, i.cpf, i.cnpj) FROM Instrutor i")
    List<InstrutorConsultaDTO> findAllInstrutoresDTO();
}
