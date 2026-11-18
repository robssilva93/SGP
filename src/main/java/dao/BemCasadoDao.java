package dao;

import model.BemCasado;
import connectionFactory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BemCasadoDao implements InterfaceDao<BemCasado> {

    private Connection conexao;
    private ProdutoDao produtoDao;

    // 1. Construtor VAZIO (uso padrão)
    public BemCasadoDao() {
        this.conexao = ConnectionFactory.getConnection();
        this.produtoDao = new ProdutoDao(this.conexao);
    }

    // 2. Construtor COM PARÂMETRO (compartilhamento de conexão)
    public BemCasadoDao(Connection conexao) {
        this.conexao = conexao;
        this.produtoDao = new ProdutoDao(this.conexao);
    }

    @Override
    public void salvar(BemCasado bemCasado) {
        String sqlBemCasado = "INSERT INTO bem_casado (id_produto, preco_unitario) VALUES (?, ?)";

        try {
            conexao.setAutoCommit(false);

            // 1. Salva na tabela pai (produto) e preenche o ID gerado no objeto
            produtoDao.salvar(bemCasado);

            // 2. Salva na tabela filha (bem_casado)
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBemCasado)) {
                stmt.setInt(1, bemCasado.getIdProduto());
                stmt.setBigDecimal(2, bemCasado.getPrecoUnidade());

                stmt.executeUpdate();
            }

            conexao.commit();

        } catch (SQLException e) {
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao salvar bem-casado: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<BemCasado> buscarPorId(int id) {
        String sql = "SELECT p.*, bc.* FROM produto p " +
                "INNER JOIN bem_casado bc ON p.id_produto = bc.id_produto " +
                "WHERE p.id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearBemCasado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar bem-casado por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<BemCasado> listarTodos() {
        String sql = "SELECT p.*, bc.* FROM produto p " +
                "INNER JOIN bem_casado bc ON p.id_produto = bc.id_produto";

        List<BemCasado> bemCasados = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bemCasados.add(mapearBemCasado(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar bem-casados: " + e.getMessage(), e);
        }
        return bemCasados;
    }

    public List<BemCasado> buscarPorNome(String nome) {
        String sql = "SELECT p.*, bc.* FROM produto p " +
                "INNER JOIN bem_casado bc ON p.id_produto = bc.id_produto " +
                "WHERE p.nome LIKE ?";

        List<BemCasado> bemCasados = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bemCasados.add(mapearBemCasado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar bem-casados por nome: " + e.getMessage(), e);
        }
        return bemCasados;
    }

    @Override
    public boolean atualizar(BemCasado bemCasado) {
        String sqlBemCasado = "UPDATE bem_casado SET preco_unitario = ? WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            // 1. Atualiza na tabela pai
            boolean produtoAtualizado = produtoDao.atualizar(bemCasado);

            // 2. Atualiza na tabela filha
            boolean bemCasadoAtualizado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBemCasado)) {
                stmt.setBigDecimal(1, bemCasado.getPrecoUnidade());
                stmt.setInt(2, bemCasado.getIdProduto());

                int linhasAfetadas = stmt.executeUpdate();
                bemCasadoAtualizado = linhasAfetadas > 0;
            }

            if (produtoAtualizado && bemCasadoAtualizado) {
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
            throw new RuntimeException("Erro ao atualizar bem-casado: " + e.getMessage(), e);

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
        String sql = "DELETE FROM bem_casado WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            boolean bemCasadoDeletado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhas = stmt.executeUpdate();
                bemCasadoDeletado = linhas > 0;
            }

            boolean produtoDeletado = produtoDao.deletar(id);

            if (bemCasadoDeletado && produtoDeletado) {
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
            throw new RuntimeException("Erro ao deletar bem-casado: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO BEM CASADO
    private BemCasado mapearBemCasado(ResultSet rs) throws SQLException {
        BemCasado bemCasado = new BemCasado();

        // Dados de Produto (Tabela PAI)
        bemCasado.setIdProduto(rs.getInt("id_produto"));
        bemCasado.setNome(rs.getString("nome"));
        bemCasado.setPrecoFinal(rs.getBigDecimal("preco_final"));
        bemCasado.setDescricao(rs.getString("descricao"));

        // Dados de BemCasado (Tabela FILHA)
        bemCasado.setPrecoUnidade(rs.getBigDecimal("preco_unitario"));

        return bemCasado;
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
