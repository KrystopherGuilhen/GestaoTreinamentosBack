//package gestao.treinamento.repository.cadastros;
//
//import gestao.treinamento.model.entidades.UnidadePerfil;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CadastroUnidadePerfilRepository extends JpaRepository<UnidadePerfil, Long> {
//
//    @Query("SELECT urt.perfil.id FROM UnidadePerfil urt WHERE urt.unidade.id = :unidadeId")
//    List<Long> findPerfilByUnidadeId(@Param("unidadeId") Long unidadeId);
//
//    boolean existsByUnidadeIdAndPerfilId(Long unidadeId, Long perfilId);
//
//    @Modifying
//    @Query("DELETE FROM UnidadePerfil urt WHERE urt.unidade.id = :unidadeId AND urt.perfil.id IN :perfilIds")
//    void deleteByUnidadeIdAndPerfilIds(@Param("unidadeId") Long unidadeId, @Param("perfilIds") List<Long> perfilIds);
//}
