package dao;

import model.Bombom;
import connectionFactory.ConnectionFactory;
import model.Sabor;
import model.TipoRecheio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BombomDao implements InterfaceDao<Bombom> {

    private Connection conexao;
    private ProdutoDao produtoDao;

    // 1. Construtor VAZIO (uso padrão)
    public BombomDao() {
        this.conexao = ConnectionFactory.getConnection();
        this.produtoDao = new ProdutoDao(this.conexao);
    }

    @Override
    public void salvar(Bombom bombom) {
        String sqlBombom = "INSERT INTO bombom (id_produto, sabor, tipo_recheio, preco_unitario) VALUES (?, ?, ?, ?)";

        try {
            conexao.setAutoCommit(false);

            // 1. Salva na tabela pai (produto) e preenche o ID gerado no objeto
            produtoDao.salvar(bombom);

            // 2. Salva na tabela filha (bombom)
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBombom)) {
                stmt.setInt(1, bombom.getIdProduto());
                stmt.setString(2, bombom.getSaborBombom().name());
                stmt.setString(3, bombom.getTipoRecheio().name());
                stmt.setBigDecimal(4, bombom.getPrecoUnidade());

                stmt.executeUpdate();
            }

            conexao.commit();

        } catch (SQLException e) {
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao salvar bombom: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Bombom> buscarPorId(int id) {
        String sql = "SELECT p.*, bm.* FROM produto p " +
                "INNER JOIN bombom bm ON p.id_produto = bm.id_produto " +
                "WHERE p.id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearBombom(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar bombom por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Bombom> listarTodos() {
        String sql = "SELECT p.*, bm.* FROM produto p " +
                "INNER JOIN bombom bm ON p.id_produto = bm.id_produto";

        List<Bombom> bombons = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bombons.add(mapearBombom(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar bombons: " + e.getMessage(), e);
        }
        return bombons;
    }

    public List<Bombom> buscarPorNome(String nome) {
        String sql = "SELECT p.*, bm.* FROM produto p " +
                "INNER JOIN bombom bm ON p.id_produto = bm.id_produto " +
                "WHERE p.nome LIKE ?";

        List<Bombom> bombons = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bombons.add(mapearBombom(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar bombons por nome: " + e.getMessage(), e);
        }
        return bombons;
    }

    @Override
    public boolean atualizar(Bombom bombom) {
        String sqlBombom = "UPDATE bombom SET sabor = ?, tipo_recheio = ?, preco_unitario = ? WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            // 1. Atualiza na tabela pai
            boolean produtoAtualizado = produtoDao.atualizar(bombom);

            // 2. Atualiza na tabela filha
            boolean bombomAtualizado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBombom)) {
                stmt.setString(1, bombom.getSaborBombom().name());
                stmt.setString(2, bombom.getTipoRecheio().name());
                stmt.setBigDecimal(3, bombom.getPrecoUnidade());
                stmt.setInt(4, bombom.getIdProduto());

                int linhasAfetadas = stmt.executeUpdate();
                bombomAtualizado = linhasAfetadas > 0;
            }

            if (produtoAtualizado && bombomAtualizado) {
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
            throw new RuntimeException("Erro ao atualizar bombom: " + e.getMessage(), e);

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
        String sql = "DELETE FROM bombom WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            boolean bombomDeletado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhas = stmt.executeUpdate();
                bombomDeletado = linhas > 0;
            }

            boolean produtoDeletado = produtoDao.deletar(id);

            if (bombomDeletado && produtoDeletado) {
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
            throw new RuntimeException("Erro ao deletar bombom: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO BOMBOM
    private Bombom mapearBombom(ResultSet rs) throws SQLException {
        Bombom bombom = new Bombom();

        // Dados de Produto (Tabela PAI)
        bombom.setIdProduto(rs.getInt("id_produto"));
        bombom.setNome(rs.getString("nome"));
        bombom.setPrecoFinal(rs.getBigDecimal("preco_final"));
        bombom.setDescricao(rs.getString("descricao"));

        // Dados de Bombom (Tabela FILHA)
        bombom.setSaborBombom(Sabor.valueOf(rs.getString("sabor")));
        bombom.setTipoRecheio(TipoRecheio.valueOf(rs.getString("tipo_recheio")));
        bombom.setPrecoUnidade(rs.getBigDecimal("preco_unitario"));

        return bombom;
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