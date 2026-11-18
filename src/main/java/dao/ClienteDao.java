package dao;

import model.Cliente;
import connectionFactory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDao implements InterfaceDao<Cliente> {

    private Connection conexao;

    public ClienteDao() {
        this.conexao = ConnectionFactory.getConnection();
    }

    // SALVAR
    @Override
    public void salvar(Cliente cliente) {

        String sql = "INSERT INTO cliente (nome, telefone, data_cadastro) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setDate(3, Date.valueOf(cliente.getDataCadastro()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente", e);
        }
    }

    // BUSCAR POR ID
    @Override
    public Optional<Cliente> buscarPorId(int id) {

        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Cliente cliente = mapearCliente(rs);
                    return Optional.of(cliente);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente", e);
        }

        return Optional.empty();
    }

    //BUSCAR POR NOME
    public List<Cliente> buscarPorNome(String nome) {
        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";
        List<Cliente> clientes = new ArrayList<>();

        // Inicia apenas o PreparedStatement no primeiro try
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            // 1. Define o parâmetro ANTES de executar a query.
            // O "%" permite que ele encontre o nome em qualquer parte da string.
            stmt.setString(1, "%" + nome + "%");

            // 2. Executa a query e gerencia o ResultSet em um try interno
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes por nome", e);
        }

        return clientes;
    }

    // LISTAR TODOS
    @Override
    public List<Cliente> listarTodos() {

        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes", e);
        }

        return clientes;
    }

    // ATUALIZAR
    @Override
    public boolean atualizar(Cliente cliente) {

        String sql = """
                UPDATE cliente
                SET nome = ?, telefone = ?, data_cadastro = ?
                WHERE id_cliente = ?
                """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setDate(3, Date.valueOf(cliente.getDataCadastro()));
            stmt.setInt(4, cliente.getIdCliente());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
    }

    // DELETAR
    @Override
    public boolean deletar(int id) {

        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente", e);
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO
    private Cliente mapearCliente(ResultSet rs) throws SQLException {

        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getDate("data_cadastro").toLocalDate()
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