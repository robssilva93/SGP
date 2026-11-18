package dao;

import model.BoloBombom;
import connectionFactory.ConnectionFactory;
import model.TipoMassa;
import model.TipoRecheio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoloBombomDao implements InterfaceDao<BoloBombom> {

    private Connection conexao;
    private ProdutoDao produtoDao;

    // 1. Construtor VAZIO (uso padrão)
    public BoloBombomDao() {
        this.conexao = ConnectionFactory.getConnection();
        this.produtoDao = new ProdutoDao(this.conexao);
    }

    @Override
    public void salvar(BoloBombom boloBombom) {
        String sqlBoloBombom = "INSERT INTO bolo_bombom (id_produto, tipo_massa, tipo_recheio, preco_unitario) VALUES (?, ?, ?, ?)";

        try {
            conexao.setAutoCommit(false);

            // 1. Salva na tabela pai (produto) e preenche o ID gerado no objeto
            produtoDao.salvar(boloBombom);

            // 2. Salva na tabela filha (bolo_bombom)
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBoloBombom)) {
                stmt.setInt(1, boloBombom.getIdProduto());
                stmt.setString(2, boloBombom.getTipoMassa().name());
                stmt.setString(3, boloBombom.getTipoRecheio().name());
                stmt.setBigDecimal(4, boloBombom.getPrecoUnidade());

                stmt.executeUpdate();
            }

            conexao.commit();

        } catch (SQLException e) {
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao salvar bolo bombom: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<BoloBombom> buscarPorId(int id) {
        String sql = "SELECT p.*, bb.* FROM produto p " +
                "INNER JOIN bolo_bombom bb ON p.id_produto = bb.id_produto " +
                "WHERE p.id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearBoloBombom(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar bolo bombom por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<BoloBombom> listarTodos() {
        String sql = "SELECT p.*, bb.* FROM produto p " +
                "INNER JOIN bolo_bombom bb ON p.id_produto = bb.id_produto";

        List<BoloBombom> bolosBombons = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bolosBombons.add(mapearBoloBombom(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar bolos bombons: " + e.getMessage(), e);
        }
        return bolosBombons;
    }

    public List<BoloBombom> buscarPorNome(String nome) {
        String sql = "SELECT p.*, bb.* FROM produto p " +
                "INNER JOIN bolo_bombom bb ON p.id_produto = bb.id_produto " +
                "WHERE p.nome LIKE ?";

        List<BoloBombom> bolosBombons = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bolosBombons.add(mapearBoloBombom(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar bolos bombons por nome: " + e.getMessage(), e);
        }
        return bolosBombons;
    }

    @Override
    public boolean atualizar(BoloBombom boloBombom) {
        String sqlBoloBombom = "UPDATE bolo_bombom SET tipo_massa = ?, tipo_recheio = ?, preco_unitario = ? WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            // 1. Atualiza na tabela pai
            boolean produtoAtualizado = produtoDao.atualizar(boloBombom);

            // 2. Atualiza na tabela filha
            boolean boloBombomAtualizado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBoloBombom)) {
                stmt.setString(1, boloBombom.getTipoMassa().name());
                stmt.setString(2, boloBombom.getTipoRecheio().name());
                stmt.setBigDecimal(3, boloBombom.getPrecoUnidade());
                stmt.setInt(4, boloBombom.getIdProduto());

                int linhasAfetadas = stmt.executeUpdate();
                boloBombomAtualizado = linhasAfetadas > 0;
            }

            if (produtoAtualizado && boloBombomAtualizado) {
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
            throw new RuntimeException("Erro ao atualizar bolo bombom: " + e.getMessage(), e);

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
        String sql = "DELETE FROM bolo_bombom WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            boolean boloBombomDeletado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhas = stmt.executeUpdate();
                boloBombomDeletado = linhas > 0;
            }

            boolean produtoDeletado = produtoDao.deletar(id);

            if (boloBombomDeletado && produtoDeletado) {
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
            throw new RuntimeException("Erro ao deletar bolo bombom: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO BOLO BOMBOM
    private BoloBombom mapearBoloBombom(ResultSet rs) throws SQLException {
        BoloBombom boloBombom = new BoloBombom();

        // Dados de Produto (Tabela PAI)
        boloBombom.setIdProduto(rs.getInt("id_produto"));
        boloBombom.setNome(rs.getString("nome"));
        boloBombom.setPrecoFinal(rs.getBigDecimal("preco_final"));
        boloBombom.setDescricao(rs.getString("descricao"));

        // Dados de BoloBombom (Tabela FILHA)
        boloBombom.setTipoMassa(TipoMassa.valueOf(rs.getString("tipo_massa")));
        boloBombom.setTipoRecheio(TipoRecheio.valueOf(rs.getString("tipo_recheio")));
        boloBombom.setPrecoUnidade(rs.getBigDecimal("preco_unitario"));

        return boloBombom;
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
