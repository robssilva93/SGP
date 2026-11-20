package dao;

import model.ItemPedido;
import connectionFactory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemPedidoDao implements InterfaceDao<ItemPedido> {

    private Connection conexao;

    // 1. Construtor VAZIO (uso padrão)
    public ItemPedidoDao() {
        this.conexao = ConnectionFactory.getConnection();
    }

    // 2. Construtor COM PARÂMETRO (essencial para o PedidoDao compartilhar a transação)
    public ItemPedidoDao(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public void salvar(ItemPedido item) {
        String sql = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco_parcial, observacoes) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getIdPedido());
            stmt.setInt(2, item.getIdProduto());
            stmt.setInt(3, item.getQuantidade());
            stmt.setBigDecimal(4, item.getValorParcial());
            stmt.setString(5, item.getObservacoes());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setIdItem(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar item do pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ItemPedido> buscarPorId(int id) {
        String sql = "SELECT * FROM item_pedido WHERE id_item_pedido = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearItemPedido(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item do pedido por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<ItemPedido> listarTodos() {
        String sql = "SELECT * FROM item_pedido";
        List<ItemPedido> itens = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                itens.add(mapearItemPedido(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens de pedido: " + e.getMessage(), e);
        }
        return itens;
    }

    // BUSCAR TODOS OS ITENS DE UM PEDIDO ESPECÍFICO
    public List<ItemPedido> buscarPorPedido(int idPedido) {
        String sql = "SELECT * FROM item_pedido WHERE id_pedido = ?";
        List<ItemPedido> itens = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapearItemPedido(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens do pedido: " + e.getMessage(), e);
        }
        return itens;
    }

    @Override
    public boolean atualizar(ItemPedido item) {
        String sql = "UPDATE item_pedido SET id_pedido = ?, id_produto = ?, quantidade = ?, preco_parcial = ?, observacoes = ? WHERE id_item_pedido = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, item.getIdPedido());
            stmt.setInt(2, item.getIdProduto());
            stmt.setInt(3, item.getQuantidade());
            stmt.setBigDecimal(4, item.getValorParcial());
            stmt.setString(5, item.getObservacoes());
            stmt.setInt(6, item.getIdItem());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item do pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM item_pedido WHERE id_item_pedido = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar item do pedido: " + e.getMessage(), e);
        }
    }

    // DELETAR TODOS OS ITENS DE UM PEDIDO (útil para o PedidoDao antes de deletar o pedido)
    public boolean deletarPorPedido(int idPedido) {
        String sql = "DELETE FROM item_pedido WHERE id_pedido = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar itens do pedido: " + e.getMessage(), e);
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO ITEM_PEDIDO
    private ItemPedido mapearItemPedido(ResultSet rs) throws SQLException {
        ItemPedido item = new ItemPedido();

        item.setIdItem(rs.getInt("id_item_pedido"));
        item.setIdPedido(rs.getInt("id_pedido"));
        item.setIdProduto(rs.getInt("id_produto"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setValorParcial(rs.getBigDecimal("preco_parcial"));
        item.setObservacoes(rs.getString("observacoes"));

        return item;
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
