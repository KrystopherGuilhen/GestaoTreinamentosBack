package gestao.treinamento.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mapeia cada tabela com a quantidade de registros esperada e o respectivo arquivo SQL.
    // Adicionamos agora cidades e estados, além das outras tabelas.
    private static final Map<String, TableData> TABLE_INITIALIZERS = new HashMap<>();

    static {
        // Tabelas de localidade
        TABLE_INITIALIZERS.put("estados", new TableData(27, "sql/estados.sql"));
        TABLE_INITIALIZERS.put("cidades", new TableData(5570, "sql/cidades.sql"));

        // Outras tabelas
        TABLE_INITIALIZERS.put("industria", new TableData(3, "sql/industria.sql"));
        TABLE_INITIALIZERS.put("nivel_permissao", new TableData(9, "sql/nivel_permissao.sql"));
        TABLE_INITIALIZERS.put("permissao_curso", new TableData(4, "sql/permissao_curso.sql"));
        TABLE_INITIALIZERS.put("permissao_empresa", new TableData(4, "sql/permissao_empresa.sql"));
        TABLE_INITIALIZERS.put("permissao_responsavel_tecnico", new TableData(4, "sql/permissao_responsavel_tecnico.sql"));
        TABLE_INITIALIZERS.put("permissao_unidade", new TableData(4, "sql/permissao_unidade.sql"));
        TABLE_INITIALIZERS.put("permissao_trabalhador", new TableData(4, "sql/permissao_trabalhador.sql"));
        TABLE_INITIALIZERS.put("permissao_palestra", new TableData(4, "sql/permissao_palestra.sql"));
        TABLE_INITIALIZERS.put("permissao_turma", new TableData(5, "sql/permissao_turma.sql"));
        TABLE_INITIALIZERS.put("permissao_perfil", new TableData(4, "sql/permissao_perfil.sql"));
        TABLE_INITIALIZERS.put("permissao_instrutor", new TableData(4, "sql/permissao_instrutor.sql"));
        TABLE_INITIALIZERS.put("pessoa", new TableData(2, "sql/pessoa.sql"));
        TABLE_INITIALIZERS.put("eventos", new TableData(6, "sql/eventos.sql"));
        TABLE_INITIALIZERS.put("modalidade", new TableData(3, "sql/modalidade.sql"));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Primeira parte: Executa os scripts de tabelas que não exigem uma verificação específica
        TABLE_INITIALIZERS.forEach((table, data) -> {
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                System.out.println("Tabela '" + table + "' - registros encontrados: " + count);
                if (count == null || count < data.getExpectedCount()) {
                    System.out.println("Tabela '" + table + "' está vazia ou incompleta. Executando script de inserção...");
                    Resource resource = new ClassPathResource(data.getScriptPath());
                    String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                    jdbcTemplate.execute(sql);
                    System.out.println("Dados inseridos na tabela '" + table + "'.");
                } else {
                    System.out.println("Tabela '" + table + "' já contém os dados esperados.");
                }
            } catch (Exception e) {
                System.err.println("Erro na inicialização da tabela " + table + ": " + e.getMessage());
            }
        });

        // Segunda parte: Verifica especificamente o usuário Administrador e suas permissões.
        verificarEInserirAdmin();
    }

    private void verificarEInserirAdmin() {
        // Declaração da variável sqlCheckPerfil no escopo do método
        String sqlCheckPerfil = "SELECT id FROM perfil WHERE email = ?";
        Integer adminId = null;

        try {
            // Tenta buscar o ID do Administrador
            adminId = jdbcTemplate.queryForObject(sqlCheckPerfil, Integer.class, "admin@teste.com.br");
            System.out.println("Usuário 'Administrador' já existe com o id: " + adminId);
        } catch (EmptyResultDataAccessException e) {
            // Se não encontrar, insere o Administrador
            System.out.println("Usuário 'Administrador' não encontrado. Inserindo...");
            String sqlInsertAdmin = "INSERT INTO perfil(nome, email, senha) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlInsertAdmin, "Administrador", "admin@teste.com.br",
                    "$2a$10$tYbkY8jUO355T0iy84kpe..vaxJ0uS6YV5diZUPemFpSvrz9.bNI.");

            // Recupera o ID do Administrador recém-inserido
            adminId = jdbcTemplate.queryForObject(sqlCheckPerfil, Integer.class, "admin@teste.com.br");
            System.out.println("Usuário 'Administrador' inserido com o id: " + adminId);
        } catch (Exception e) {
            System.err.println("Erro ao verificar/inserir o Administrador: " + e.getMessage());
            return; // Encerra o método em caso de erro
        }

        // Verifica e insere as permissões do Administrador
        verificarEInserirPermissoes(adminId);
        verificarEInserirPermissoesPerfil(adminId);
    }

    private void verificarEInserirPermissoes(Integer adminId) {
        try {
            // Lista de níveis de permissão que o Administrador deve ter
            List<Integer> permissoesNecessarias = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9);

            for (Integer nivelPermissao : permissoesNecessarias) {
                String sqlCheckPermissao = "SELECT COUNT(*) FROM perfil_nivel_permissao WHERE perfil_id = ? AND nivel_permissao_id = ?";
                Integer count = jdbcTemplate.queryForObject(
                        sqlCheckPermissao,
                        new Object[]{adminId, nivelPermissao},
                        Integer.class);

                if (count == 0) {
                    // Insere a permissão faltante
                    jdbcTemplate.update(
                            "INSERT INTO perfil_nivel_permissao (nivel_permissao_id, perfil_id) VALUES (?, ?)",
                            nivelPermissao, adminId);
                    System.out.println("Permissão " + nivelPermissao + " adicionada ao Administrador.");
                }
            }
            System.out.println("Permissões do Administrador verificadas com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao verificar/inserir as permissões do Administrador: " + e.getMessage());
        }
    }

    private void verificarEInserirPermissoesPerfil(Integer adminId) {
        try {
            // Permissões que o Administrador deve ter na nova tabela
            List<Integer> permissoesPerfil = Arrays.asList(1, 2, 3, 4);

            for (Integer permissaoId : permissoesPerfil) {
                String sqlCheck = "SELECT COUNT(*) FROM perfil_permissao_perfil WHERE perfil_id = ? AND permissao_perfil_id = ?";
                Integer count = jdbcTemplate.queryForObject(
                        sqlCheck,
                        new Object[]{adminId, permissaoId},
                        Integer.class
                );

                if (count == 0) {
                    jdbcTemplate.update(
                            "INSERT INTO perfil_permissao_perfil (perfil_id, permissao_perfil_id) VALUES (?, ?)",
                            adminId, permissaoId
                    );
                    System.out.println("Permissão de perfil " + permissaoId + " adicionada ao Administrador.");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao inserir permissões de perfil: " + e.getMessage());
        }
    }

    // Classe auxiliar para agrupar dados necessários para a inicialização de cada tabela
    private static class TableData {
        private final int expectedCount;
        private final String scriptPath;

        public TableData(int expectedCount, String scriptPath) {
            this.expectedCount = expectedCount;
            this.scriptPath = scriptPath;
        }

        public int getExpectedCount() {
            return expectedCount;
        }

        public String getScriptPath() {
            return scriptPath;
        }
    }
}