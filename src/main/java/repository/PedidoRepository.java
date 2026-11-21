package repository;

import dao.PedidoDao;
import model.Pedido;

import java.util.List;
import java.util.Optional;

public class PedidoRepository implements InterfaceRepository<Pedido> {
    private final PedidoDao dao;
    public PedidoRepository(PedidoDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(Pedido pedido) {
        dao.salvar(pedido);
    }


    @Override
    public Optional buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List listarTodos() {
        return dao.listarTodos();
    }


    @Override
    public boolean atualizar(Pedido pedido) {
        return dao.atualizar(pedido);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
