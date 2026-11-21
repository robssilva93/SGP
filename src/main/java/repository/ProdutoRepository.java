package repository;

import dao.ProdutoDao;
import model.Produto;

import java.util.List;
import java.util.Optional;

public class ProdutoRepository implements InterfaceRepository<Produto> {

    private final ProdutoDao dao;
    public ProdutoRepository(ProdutoDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(Produto produto) {
        dao.salvar(produto);
    }

    @Override
    public Optional<Produto> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<Produto> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(Produto produto) {
        return dao.atualizar(produto);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
