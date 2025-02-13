package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoTurma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoTurmaRepository extends JpaRepository<PerfilPermissaoTurma, Long> {

    @Query("SELECT ppt.permissaoTurma.id FROM PerfilPermissaoTurma ppt WHERE ppt.perfil.id = :perfilId")
    List<Long> findPermissaoTurmaByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoTurmaId(Long perfilId, Long permissaoTurmaId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoTurma ppt WHERE ppt.perfil.id = :perfilId AND ppt.permissaoTurma.id IN :permissaoTurmaIds")
    void deleteByPerfilIdAndPermissaoTurmaIds(@Param("perfilId") Long perfilId, @Param("permissaoTurmaIds") List<Long> permissaoTurmaIds);
}
