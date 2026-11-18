package dao;

import model.Bolo;
import connectionFactory.ConnectionFactory;
import model.TipoCobertura;
import model.TipoMassa;
import model.TipoRecheio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoloDao implements InterfaceDao<Bolo> {

    private Connection conexao;
    private ProdutoDao produtoDao;

    // 1. Construtor VAZIO (usado quando você for instanciar apenas o BoloDao)
    public BoloDao() {
        this.conexao = ConnectionFactory.getConnection();
        this.produtoDao = new ProdutoDao(this.conexao); // Passa a mesma conexão para o pai
    }

    @Override
    public void salvar(Bolo bolo) {
        String sqlBolo = "INSERT INTO bolo (id_produto, diametro, peso, tipo_massa, tipo_cobertura, andares, preco_diametro, tipo_recheio) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Desliga o salvamento automático para garantir que Produto e Bolo sejam salvos juntos
            conexao.setAutoCommit(false);

            // 1. Salva na tabela pai (gera o ID e já coloca no objeto 'bolo')
            produtoDao.salvar(bolo);

            // 2. Salva na tabela filha usando o ID gerado
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBolo)) {
                stmt.setInt(1, bolo.getIdProduto());
                stmt.setFloat(2, bolo.getDiametro());
                stmt.setFloat(3, bolo.getPeso());
                stmt.setString(4, bolo.getTipoMassa().name());
                stmt.setString(5, bolo.getTipoCobertura().name());
                stmt.setInt(6, bolo.getAndares());
                stmt.setBigDecimal(7, bolo.getPrecoDiametro());
                stmt.setString(8, bolo.getTipoRecheio().name());

                stmt.executeUpdate();
            }

            // Se chegou até aqui sem dar erro, efetiva a transação nas duas tabelas
            conexao.commit();

        } catch (SQLException e) {
            // Se der erro em qualquer lugar (no pai ou no filho), desfaz tudo
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao salvar bolo: " + e.getMessage(), e);

        } finally {
            // Retorna a conexão ao estado normal
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Bolo> buscarPorId(int id) {
        // Fazendo JOIN para trazer os dados do pai e do filho de uma vez só
        String sql = "SELECT p.*, b.* FROM produto p " +
                "INNER JOIN bolo b ON p.id_produto = b.id_produto " +
                "WHERE p.id_produto = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Bolo bolo = mapearBolo(rs);
                    return Optional.of(bolo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar bolo por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Bolo> listarTodos() {
        String sql = "SELECT p.*, b.* FROM produto p " +
                "INNER JOIN bolo b ON p.id_produto = b.id_produto";

        List<Bolo> bolos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bolos.add(mapearBolo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar bolos: " + e.getMessage(), e);
        }
        return bolos;
    }

    public List<Bolo> buscarPorNome(String nome) {
        String sql = "SELECT p.*, b.* FROM produto p " +
                "INNER JOIN bolo b ON p.id_produto = b.id_produto " +
                "WHERE p.nome LIKE ?";

        List<Bolo> bolos = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bolos.add(mapearBolo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar bolos por nome: " + e.getMessage(), e);
        }

        return bolos;
    }

    @Override
    public boolean atualizar(Bolo bolo) {
        String sqlBolo = "UPDATE bolo SET diametro = ?, peso = ?, tipo_massa = ?, tipo_cobertura = ?, andares = ?, preco_diametro = ?, tipo_recheio = ? WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            // 1. Atualiza os dados genéricos na tabela produto
            boolean produtoAtualizado = produtoDao.atualizar(bolo);

            // 2. Atualiza os dados específicos na tabela bolo
            boolean boloAtualizado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlBolo)) {
                stmt.setFloat(1, bolo.getDiametro());
                stmt.setFloat(2, bolo.getPeso());
                stmt.setString(3, bolo.getTipoMassa().name());
                stmt.setString(4, bolo.getTipoCobertura().name());
                stmt.setInt(5, bolo.getAndares());
                stmt.setBigDecimal(6, bolo.getPrecoDiametro());
                stmt.setString(7, bolo.getTipoRecheio().name());
                stmt.setInt(8, bolo.getIdProduto());

                int linhasAfetadas = stmt.executeUpdate();
                boloAtualizado = linhasAfetadas > 0;
            }

            // Se ambas as atualizações deram certo, efetiva.
            if (produtoAtualizado && boloAtualizado) {
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
            throw new RuntimeException("Erro ao atualizar bolo: " + e.getMessage(), e);

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
        // Atenção à ordem: Deleta primeiro o FILHO (bolo) e depois o PAI (produto)
        String sql = "DELETE FROM bolo WHERE id_produto = ?";

        try {
            conexao.setAutoCommit(false);

            // 1. Deleta de bolo
            boolean boloDeletado = false;
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhas = stmt.executeUpdate();
                boloDeletado = linhas > 0;
            }

            // 2. Deleta de produto (usando o ProdutoDao)
            boolean produtoDeletado = produtoDao.deletar(id);

            // Só confirma a deleção se apagou das duas tabelas
            if (boloDeletado && produtoDeletado) {
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
            throw new RuntimeException("Erro ao deletar bolo: " + e.getMessage(), e);

        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // MAPEAMENTO DO RESULTSET PARA OBJETO BOLO
    private Bolo mapearBolo(ResultSet rs) throws SQLException {
        // Optei por instanciar vazio e usar os Setters para não ter perigo
        // de você esbarrar na ordem dos parâmetros do construtor cheio.
        Bolo bolo = new Bolo();

        // Dados de Produto (Tabela PAI)
        bolo.setIdProduto(rs.getInt("id_produto"));
        bolo.setNome(rs.getString("nome"));
        bolo.setPrecoFinal(rs.getBigDecimal("preco_final"));
        bolo.setDescricao(rs.getString("descricao"));

        // Dados de Bolo (Tabela FILHA)
        bolo.setDiametro(rs.getFloat("diametro"));
        bolo.setPeso(rs.getFloat("peso"));
        bolo.setTipoMassa(TipoMassa.valueOf(rs.getString("tipo_massa")));
        bolo.setTipoCobertura(TipoCobertura.valueOf(rs.getString("tipo_cobertura")));
        bolo.setAndares(rs.getInt("andares"));
        bolo.setPrecoDiametro(rs.getBigDecimal("preco_diametro"));
        bolo.setTipoRecheio(TipoRecheio.valueOf(rs.getString("tipo_recheio")));

        return bolo;
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
