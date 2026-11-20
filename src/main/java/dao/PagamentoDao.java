package dao;

import model.Pagamento;
import connectionFactory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagamentoDao implements InterfaceDao<Pagamento> {

    private Connection conexao;

    // 1. Construtor VAZIO (uso padrão)
    public PagamentoDao() {
        this.conexao = ConnectionFactory.getConnection();
    }

    // 2. Construtor COM PARÂMETRO (compartilhamento de conexão/transação com PedidoDao)
    public PagamentoDao(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public void salvar(Pagamento pagamento) {
        String sql = "INSERT INTO pagamento (id_pedido, valor_pago, data_pagamento, comprovante) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pagamento.getIdPedido());
            stmt.setBigDecimal(2, pagamento.getValorPago());
            stmt.setDate(3, Date.valueOf(pagamento.getDataPagamento()));
            stmt.setString(4, pagamento.getComprovante());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pagamento.setIdPagamento(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar pagamento: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Pagamento> buscarPorId(int id) {
        String sql = "SELECT * FROM pagamento WHERE id_pagamento = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPagamento(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pagamento por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Pagamento> listarTodos() {
        String sql = "SELECT * FROM pagamento";
        List<Pagamento> pagamentos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pagamentos.add(mapearPagamento(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pagamentos: " + e.getMessage(), e);
        }
        return pagamentos;
    }

    // BUSCAR TODOS OS PAGAMENTOS DE UM PEDIDO ESPECÍFICO
    public List<Pagamento> buscarPorPedido(int idPedido) {
        String sql = "SELECT * FROM pagamento WHERE id_pedido = ?";
        List<Pagamento> pagamentos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pagamentos.add(mapearPagamento(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pagamentos do pedido: " + e.getMessage(), e);
        }
        return pagamentos;
    }

    @Override
    public boolean atualizar(Pagamento pagamento) {
        String sql = "UPDATE pagamento SET id_pedido = ?, valor_pago = ?, data_pagamento = ?, comprovante = ? WHERE id_pagamento = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pagamento.getIdPedido());
            stmt.setBigDecimal(2, pagamento.getValorPago());
            stmt.setDate(3, Date.valueOf(pagamento.getDataPagamento()));
            stmt.setString(4, pagamento.getComprovante());
            stmt.setInt(5, pagamento.getIdPagamento());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pagamento: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM pagamento WHERE id_pagamento = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pagamento: " + e.getMessage(), e);
        }
    }

    // DELETAR TODOS OS PAGAMENTOS DE UM PEDIDO (útil para o PedidoDao antes de deletar o pedido)
    public boolean deletarPorPedido(int idPedido) {
        String sql = "DELETE FROM pagamento WHERE id_pedido = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pagamentos do pedido: " + e.getMessage(), e);
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO PAGAMENTO
    private Pagamento mapearPagamento(ResultSet rs) throws SQLException {
        Pagamento pagamento = new Pagamento();

        pagamento.setIdPagamento(rs.getInt("id_pagamento"));
        pagamento.setIdPedido(rs.getInt("id_pedido"));
        pagamento.setValorPago(rs.getBigDecimal("valor_pago"));

        Date dataSql = rs.getDate("data_pagamento");
        if (dataSql != null) {
            pagamento.setDataPagamento(dataSql.toLocalDate());
        }

        pagamento.setComprovante(rs.getString("comprovante"));

        return pagamento;
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
