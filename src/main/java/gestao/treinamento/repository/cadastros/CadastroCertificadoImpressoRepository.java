package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.CertificadoImpresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroCertificadoImpressoRepository extends JpaRepository<CertificadoImpresso, Long> {
}
