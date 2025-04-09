package gestao.treinamento.repository.cadastros;

import gestao.treinamento.model.entidades.PerfilPermissaoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroPerfilPermissaoCursoRepository extends JpaRepository<PerfilPermissaoCurso, Long> {

    @Query("SELECT ppc.permissaoCurso.id FROM PerfilPermissaoCurso ppc WHERE ppc.perfil.id = :perfilId")
    List<Long> findPermissaoCursoByPerfilId(@Param("perfilId") Long perfilId);

    boolean existsByPerfilIdAndPermissaoCursoId(Long perfilId, Long permissaoCursoId);

    @Modifying
    @Query("DELETE FROM PerfilPermissaoCurso ppc WHERE ppc.perfil.id = :perfilId AND ppc.permissaoCurso.id IN :permissaoCursoIds")
    void deleteByPerfilIdAndPermissaoCursoIds(@Param("perfilId") Long perfilId, @Param("permissaoCursoIds") List<Long> permissaoCursoIds);

    void deleteByPerfilId(Long perfilId);
}
