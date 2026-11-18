package dao;

import model.Brigadeiro;
import connectionFactory.ConnectionFactory;
import model.SaborBrigadeiro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrigadeiroDao implements InterfaceDao<Brigadeiro> {

    private Connection conexao;
    private ProdutoDao produtoDao;

    // 1. Construtor VAZIO (uso padrão)
    public BrigadeiroDao() {
        this.conexao = ConnectionFactory.getConnection();
        this.produtoDao = new ProdutoDao(this.conexao);
    }

    @Override
    public void salvar(Brigadeiro brigadeiro) {
        String sqlBrigadeiro = "INSERT INTO brigadeiro (id_produto, personalizado, tema, sabor, preco_unitario) VALUES (?, ?, ?, ?, ?)";

        try {
            conexao.setAutoCommit(false);

            // 1. Salva na tabela pai (produto) e preenche o ID gerado no objeto
            produtoDao.salvar(brigadeiro);

            // 2. Salva na tabela filha (brigadeiro)
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBrigadeiro)) {
                stmt.setInt(1, brigadeiro.getIdProduto());
                stmt.setBoolean(2, brigadeiro.isPersonalizado());
                stmt.setString(3, brigadeiro.getTema());
                stmt.setString(4, brigadeiro.getSaborBrigadeiro().name());
                stmt.setBigDecimal(5, brigadeiro.getPrecoUnidade());

                stmt.executeUpdate();
            }

            conexao.commit();

        } catch (SQLException e) {
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao salvar brigadeiro: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Brigadeiro> buscarPorId(int id) {
        String sql = "SELECT p.*, br.* FROM produto p " +
                "INNER JOIN brigadeiro br ON p.id_produto = br.id_produto " +
                "WHERE p.id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearBrigadeiro(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar brigadeiro por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Brigadeiro> listarTodos() {
        String sql = "SELECT p.*, br.* FROM produto p " +
                "INNER JOIN brigadeiro br ON p.id_produto = br.id_produto";

        List<Brigadeiro> brigadeiros = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                brigadeiros.add(mapearBrigadeiro(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar brigadeiros: " + e.getMessage(), e);
        }
        return brigadeiros;
    }

    public List<Brigadeiro> buscarPorNome(String nome) {
        String sql = "SELECT p.*, br.* FROM produto p " +
                "INNER JOIN brigadeiro br ON p.id_produto = br.id_produto " +
                "WHERE p.nome LIKE ?";

        List<Brigadeiro> brigadeiros = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    brigadeiros.add(mapearBrigadeiro(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar brigadeiros por nome: " + e.getMessage(), e);
        }
        return brigadeiros;
    }

    @Override
    public boolean atualizar(Brigadeiro brigadeiro) {
        String sqlBrigadeiro = "UPDATE brigadeiro SET personalizado = ?, tema = ?, sabor = ?, preco_unitario = ? WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            // 1. Atualiza na tabela pai
            boolean produtoAtualizado = produtoDao.atualizar(brigadeiro);

            // 2. Atualiza na tabela filha
            boolean brigadeiroAtualizado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBrigadeiro)) {
                stmt.setBoolean(1, brigadeiro.isPersonalizado());
                stmt.setString(2, brigadeiro.getTema());
                stmt.setString(3, brigadeiro.getSaborBrigadeiro().name());
                stmt.setBigDecimal(4, brigadeiro.getPrecoUnidade());
                stmt.setInt(5, brigadeiro.getIdProduto());

                int linhasAfetadas = stmt.executeUpdate();
                brigadeiroAtualizado = linhasAfetadas > 0;
            }

            if (produtoAtualizado && brigadeiroAtualizado) {
                conexao.commit();
                return true;
            } else {
                conexao.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao atualizar brigadeiro: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean deletar(int id) {
        // Deleta primeiro da tabela filha, depois da tabela pai
        String sql = "DELETE FROM brigadeiro WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            boolean brigadeiroDeletado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhas = stmt.executeUpdate();
                brigadeiroDeletado = linhas > 0;
            }

            boolean produtoDeletado = produtoDao.deletar(id);

            if (brigadeiroDeletado && produtoDeletado) {
                conexao.commit();
                return true;
            } else {
                conexao.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback no deletar: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao deletar brigadeiro: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO BRIGADEIRO
    private Brigadeiro mapearBrigadeiro(ResultSet rs) throws SQLException {
        Brigadeiro brigadeiro = new Brigadeiro();

        // Dados de Produto (Tabela PAI)
        brigadeiro.setIdProduto(rs.getInt("id_produto"));
        brigadeiro.setNome(rs.getString("nome"));
        brigadeiro.setPrecoFinal(rs.getBigDecimal("preco_final"));
        brigadeiro.setDescricao(rs.getString("descricao"));

        // Dados de Brigadeiro (Tabela FILHA)
        brigadeiro.setPersonalizado(rs.getBoolean("personalizado"));
        brigadeiro.setTema(rs.getString("tema"));
        brigadeiro.setSaborBrigadeiro(SaborBrigadeiro.valueOf(rs.getString("sabor")));
        brigadeiro.setPrecoUnidade(rs.getBigDecimal("preco_unitario"));

        return brigadeiro;
    }

    // FECHAR CONEXÃO
    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexão", e);
        }
    }
}
