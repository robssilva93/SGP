package dao;

import model.Produto;
import connectionFactory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDao implements InterfaceDao<Produto> {

    private Connection conexao;

    // A conexão é injetada via construtor
    public ProdutoDao() {
        this.conexao = ConnectionFactory.getConnection();
    }

    // 2. Construtor COM PARÂMETRO (usado pelas classes filhas como BoloDao)
    public ProdutoDao(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public void salvar(Produto produto) {
        String sql = "INSERT INTO produto (nome, preco_final, descricao) VALUES (?, ?, ?)";

        // Statement.RETURN_GENERATED_KEYS é crucial aqui para as classes filhas
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPrecoFinal());
            stmt.setString(3, produto.getDescricao());

            stmt.executeUpdate();

            // Recupera o ID gerado no banco e atribui ao objeto
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setIdProduto(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Produto> buscarPorId(int id) {
        String sql = "SELECT * FROM produto WHERE id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = mapearProduto(rs);

                    return Optional.of(produto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
        return produtos;
    }

    //BUSCAR POR NOME
    public List<Produto> buscarPorNome(String nome) {
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        List<Produto> produtos = new ArrayList<>();

        // Inicia apenas o PreparedStatement no primeiro try
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            // 1. Define o parâmetro ANTES de executar a query.
            // O "%" permite que ele encontre o nome em qualquer parte da string.
            stmt.setString(1, "%" + nome + "%");

            // 2. Executa a query e gerencia o ResultSet em um try interno
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapearProduto(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos por nome", e);
        }

        return produtos;
    }

    @Override
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, preco_final = ?, descricao = ? WHERE id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPrecoFinal());
            stmt.setString(3, produto.getDescricao());
            stmt.setInt(4, produto.getIdProduto());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM produto WHERE id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto: " + e.getMessage(), e);
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO
    private Produto mapearProduto(ResultSet rs) throws SQLException {

        return new Produto(
                rs.getInt("id_produto"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getBigDecimal("preco_final")
        );
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
