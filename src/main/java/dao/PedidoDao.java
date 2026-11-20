package dao;

import model.*;
import connectionFactory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDao implements InterfaceDao<Pedido> {

    private Connection conexao;
    private ItemPedidoDao itemPedidoDao;
    private PagamentoDao pagamentoDao;

    // 1. Construtor VAZIO (uso padrão)
    public PedidoDao() {
        this.conexao = ConnectionFactory.getConnection();
        this.itemPedidoDao = new ItemPedidoDao(this.conexao);
        this.pagamentoDao = new PagamentoDao(this.conexao);
    }

    // 2. Construtor COM PARÂMETRO (compartilhamento de conexão)
    public PedidoDao(Connection conexao) {
        this.conexao = conexao;
        this.itemPedidoDao = new ItemPedidoDao(this.conexao);
        this.pagamentoDao = new PagamentoDao(this.conexao);
    }

    @Override
    public void salvar(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_cliente, id_funcionario, data_pedido, data_agendada, horario, " +
                "valor_total, valor_pago, status, tipo_atendimento, endereco, especificidades) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conexao.setAutoCommit(false);

            // 1. Salva o pedido e recupera o ID gerado
            try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preencherStatement(stmt, pedido);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setIdPedido(rs.getInt(1));
                    }
                }
            }

            // 2. Salva os itens vinculados ao pedido (se houver)
            if (pedido.getItensPedido() != null) {
                for (ItemPedido item : pedido.getItensPedido()) {
                    item.setIdPedido(pedido.getIdPedido());
                    itemPedidoDao.salvar(item);
                }
            }

            // 3. Salva os pagamentos vinculados ao pedido (se houver)
            if (pedido.getPagamentos() != null) {
                for (Pagamento pagamento : pedido.getPagamentos()) {
                    pagamento.setIdPedido(pedido.getIdPedido());
                    pagamentoDao.salvar(pagamento);
                }
            }

            conexao.commit();

        } catch (SQLException e) {
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao salvar pedido: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Pedido> buscarPorId(int id) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Pedido> listarTodos() {
        String sql = "SELECT * FROM pedido";
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos: " + e.getMessage(), e);
        }
        return pedidos;
    }

    // BUSCAR PEDIDOS POR CLIENTE
    public List<Pedido> buscarPorCliente(int idCliente) {
        String sql = "SELECT * FROM pedido WHERE id_cliente = ?";
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedidos por cliente: " + e.getMessage(), e);
        }
        return pedidos;
    }

    // BUSCAR PEDIDOS POR STATUS
    public List<Pedido> buscarPorStatus(String status) {
        String sql = "SELECT * FROM pedido WHERE status = ?";
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedidos por status: " + e.getMessage(), e);
        }
        return pedidos;
    }

    @Override
    public boolean atualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET id_cliente = ?, id_funcionario = ?, data_pedido = ?, data_agendada = ?, " +
                "horario = ?, valor_total = ?, valor_pago = ?, status = ?, tipo_atendimento = ?, endereco = ?, especificidades = ? " +
                "WHERE id_pedido = ?";

        try {
            conexao.setAutoCommit(false);

            boolean pedidoAtualizado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                preencherStatement(stmt, pedido);
                stmt.setInt(12, pedido.getIdPedido());

                int linhasAfetadas = stmt.executeUpdate();
                pedidoAtualizado = linhasAfetadas > 0;
            }

            if (pedidoAtualizado) {
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
            throw new RuntimeException("Erro ao atualizar pedido: " + e.getMessage(), e);

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
        // Ordem obrigatória por causa das Foreign Keys:
        // 1. Deleta itens -> 2. Deleta pagamentos -> 3. Deleta o pedido
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";

        try {
            conexao.setAutoCommit(false);

            itemPedidoDao.deletarPorPedido(id);
            pagamentoDao.deletarPorPedido(id);

            boolean pedidoDeletado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhas = stmt.executeUpdate();
                pedidoDeletado = linhas > 0;
            }

            if (pedidoDeletado) {
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
            throw new RuntimeException("Erro ao deletar pedido: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // AUXILIAR PARA PREENCHER OS 11 CAMPOS COMUNS DE INSERT E UPDATE
    private void preencherStatement(PreparedStatement stmt, Pedido pedido) throws SQLException {
        stmt.setInt(1, pedido.getIdCliente());
        stmt.setInt(2, pedido.getIdFuncionario());
        stmt.setDate(3, Date.valueOf(pedido.getDataPedido()));

        if (pedido.getDataAgendada() != null) {
            stmt.setDate(4, Date.valueOf(pedido.getDataAgendada()));
        } else {
            stmt.setNull(4, Types.DATE);
        }

        if (pedido.getHoraAgendada() != null) {
            stmt.setTime(5, Time.valueOf(pedido.getHoraAgendada()));
        } else {
            stmt.setNull(5, Types.TIME);
        }

        stmt.setBigDecimal(6, pedido.getValorTotal());
        stmt.setBigDecimal(7, pedido.getValorPago());
        stmt.setString(8, pedido.getStatus().name());
        stmt.setString(9, pedido.getTipoAtendimento().name());
        stmt.setString(10, pedido.getEndereco());
        stmt.setString(11, pedido.getDetalhesEspecificos());
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO PEDIDO (JÁ CARREGANDO ITENS E PAGAMENTOS)
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        int idPedido = rs.getInt("id_pedido");

        pedido.setIdPedido(idPedido);
        pedido.setIdCliente(rs.getInt("id_cliente"));
        pedido.setIdFuncionario(rs.getInt("id_funcionario"));

        Date dataPedidoSql = rs.getDate("data_pedido");
        if (dataPedidoSql != null) {
            pedido.setDataPedido(dataPedidoSql.toLocalDate());
        }

        Date dataAgendadaSql = rs.getDate("data_agendada");
        if (dataAgendadaSql != null) {
            pedido.setDataAgendada(dataAgendadaSql.toLocalDate());
        }

        Time horarioSql = rs.getTime("horario");
        if (horarioSql != null) {
            pedido.setHoraAgendada(horarioSql.toLocalTime());
        }

        pedido.setValorTotal(rs.getBigDecimal("valor_total"));
        pedido.setValorPago(rs.getBigDecimal("valor_pago"));
        pedido.setStatus(StatusPedido.valueOf(rs.getString("status")));
        pedido.setTipoAtendimento(TipoAtendimento.valueOf(rs.getString("tipo_atendimento")));
        pedido.setEndereco(rs.getString("endereco"));
        pedido.setDetalhesEspecificos(rs.getString("especificidades"));

        // Carrega os registros filhos automaticamente
        pedido.setItensPedido(itemPedidoDao.buscarPorPedido(idPedido));
        pedido.setPagamentos(pagamentoDao.buscarPorPedido(idPedido));

        return pedido;
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