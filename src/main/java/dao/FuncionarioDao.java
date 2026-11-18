package dao;

import connectionFactory.ConnectionFactory;
import model.Funcionario;
import model.TipoFuncionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionarioDao implements InterfaceDao<Funcionario>{

    private Connection conexao;

    public FuncionarioDao() {
        this.conexao = ConnectionFactory.getConnection();
    }

    // SALVAR
    @Override
    public void salvar(Funcionario funcionario) {

        String sql = "INSERT INTO funcionario (nome, login, senha, cargo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNomeFuncionario());
            stmt.setString(2, funcionario.getLogin());
            stmt.setString(3, funcionario.getSenha());
            stmt.setString(4, funcionario.getCargo().name());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar funcionário", e);
        }
    }

    // BUSCAR POR ID
    @Override
    public Optional<Funcionario> buscarPorId(int id) {

        String sql = "SELECT * FROM funcionario WHERE id_funcionario = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Funcionario funcionario = mapearFuncionario(rs);
                    return Optional.of(funcionario);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário", e);
        }

        return Optional.empty();
    }

    //BUSCAR POR NOME
    public List<Funcionario> buscarPorNome(String nome) {
        String sql = "SELECT * FROM funcionario WHERE nome LIKE ?";
        List<Funcionario> funcionarios = new ArrayList<>();

        // Inicia apenas o PreparedStatement no primeiro try
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            // 1. Define o parâmetro ANTES de executar a query.
            // O "%" permite que ele encontre o nome em qualquer parte da string.
            stmt.setString(1, "%" + nome + "%");

            // 2. Executa a query e gerencia o ResultSet em um try interno
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    funcionarios.add(mapearFuncionario(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionarios por nome", e);
        }

        return funcionarios;
    }

    // LISTAR TODOS
    @Override
    public List<Funcionario> listarTodos() {

        String sql = "SELECT * FROM funcionario";
        List<Funcionario> funcionarios = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                funcionarios.add(mapearFuncionario(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários", e);
        }

        return funcionarios;
    }

    // ATUALIZAR
    @Override
    public boolean atualizar(Funcionario funcionario) {

        String sql = """
                UPDATE funcionario
                SET nome = ?, login = ?, senha = ?, cargo = ?
                WHERE id_funcionario = ?
                """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNomeFuncionario());
            stmt.setString(2, funcionario.getLogin());
            stmt.setString(3, funcionario.getSenha());
            stmt.setString(4, funcionario.getCargo().name());
            stmt.setInt(5, funcionario.getIdFuncionario());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar funcionário", e);
        }
    }

    // DELETAR
    @Override
    public boolean deletar(int id) {

        String sql = "DELETE FROM funcionario WHERE id_funcionario = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar funcionário", e);
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO
    private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {

        return new Funcionario(
                rs.getInt("id_funcionario"),
                rs.getString("nome"),
                rs.getString("login"),
                rs.getString("senha"),
                TipoFuncionario.valueOf(rs.getString("cargo"))
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
