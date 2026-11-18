package dao;

import model.PirulitoChocolate;
import connectionFactory.ConnectionFactory;
import model.Sabor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PirulitoChocolateDao implements InterfaceDao<PirulitoChocolate> {

    private Connection conexao;
    private ProdutoDao produtoDao;

    // 1. Construtor VAZIO (uso padrão)
    public PirulitoChocolateDao() {
        this.conexao = ConnectionFactory.getConnection();
        this.produtoDao = new ProdutoDao(this.conexao);
    }

    @Override
    public void salvar(PirulitoChocolate pirulito) {
        String sqlPirulito = "INSERT INTO pirulito_chocolate (id_produto, sabor, preco_unitario) VALUES (?, ?, ?)";

        try {
            conexao.setAutoCommit(false);

            // 1. Salva na tabela pai (produto) e preenche o ID gerado no objeto
            produtoDao.salvar(pirulito);

            // 2. Salva na tabela filha (pirulito_chocolate)
            try (PreparedStatement stmt = conexao.prepareStatement(sqlPirulito)) {
                stmt.setInt(1, pirulito.getIdProduto());
                stmt.setString(2, pirulito.getSaborPirulito().name());
                stmt.setBigDecimal(3, pirulito.getPrecoUnidade());

                stmt.executeUpdate();
            }

            conexao.commit();

        } catch (SQLException e) {
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao salvar pirulito de chocolate: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<PirulitoChocolate> buscarPorId(int id) {
        String sql = "SELECT p.*, pc.* FROM produto p " +
                "INNER JOIN pirulito_chocolate pc ON p.id_produto = pc.id_produto " +
                "WHERE p.id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPirulitoChocolate(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pirulito de chocolate por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<PirulitoChocolate> listarTodos() {
        String sql = "SELECT p.*, pc.* FROM produto p " +
                "INNER JOIN pirulito_chocolate pc ON p.id_produto = pc.id_produto";

        List<PirulitoChocolate> pirulitos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pirulitos.add(mapearPirulitoChocolate(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pirulitos de chocolate: " + e.getMessage(), e);
        }
        return pirulitos;
    }

    public List<PirulitoChocolate> buscarPorNome(String nome) {
        String sql = "SELECT p.*, pc.* FROM produto p " +
                "INNER JOIN pirulito_chocolate pc ON p.id_produto = pc.id_produto " +
                "WHERE p.nome LIKE ?";

        List<PirulitoChocolate> pirulitos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pirulitos.add(mapearPirulitoChocolate(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pirulitos de chocolate por nome: " + e.getMessage(), e);
        }
        return pirulitos;
    }

    @Override
    public boolean atualizar(PirulitoChocolate pirulito) {
        String sqlPirulito = "UPDATE pirulito_chocolate SET sabor = ?, preco_unitario = ? WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            // 1. Atualiza na tabela pai
            boolean produtoAtualizado = produtoDao.atualizar(pirulito);

            // 2. Atualiza na tabela filha
            boolean pirulitoAtualizado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlPirulito)) {
                stmt.setString(1, pirulito.getSaborPirulito().name());
                stmt.setBigDecimal(2, pirulito.getPrecoUnidade());
                stmt.setInt(3, pirulito.getIdProduto());

                int linhasAfetadas = stmt.executeUpdate();
                pirulitoAtualizado = linhasAfetadas > 0;
            }

            if (produtoAtualizado && pirulitoAtualizado) {
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
            throw new RuntimeException("Erro ao atualizar pirulito de chocolate: " + e.getMessage(), e);

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
        String sql = "DELETE FROM pirulito_chocolate WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            boolean pirulitoDeletado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhas = stmt.executeUpdate();
                pirulitoDeletado = linhas > 0;
            }

            boolean produtoDeletado = produtoDao.deletar(id);

            if (pirulitoDeletado && produtoDeletado) {
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
            throw new RuntimeException("Erro ao deletar pirulito de chocolate: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO PIRULITO DE CHOCOLATE
    private PirulitoChocolate mapearPirulitoChocolate(ResultSet rs) throws SQLException {
        PirulitoChocolate pirulito = new PirulitoChocolate();

        // Dados de Produto (Tabela PAI)
        pirulito.setIdProduto(rs.getInt("id_produto"));
        pirulito.setNome(rs.getString("nome"));
        pirulito.setPrecoFinal(rs.getBigDecimal("preco_final"));
        pirulito.setDescricao(rs.getString("descricao"));

        // Dados de PirulitoChocolate (Tabela FILHA)
        pirulito.setSaborPirulito(Sabor.valueOf(rs.getString("sabor")));
        pirulito.setPrecoUnidade(rs.getBigDecimal("preco_unitario"));

        return pirulito;
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
