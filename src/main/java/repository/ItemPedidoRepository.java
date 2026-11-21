package repository;

import dao.ItemPedidoDao;
import model.ItemPedido;

import java.util.List;
import java.util.Optional;

public class ItemPedidoRepository  implements InterfaceRepository<ItemPedido>{

    private final ItemPedidoDao dao;
    public ItemPedidoRepository(ItemPedidoDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(ItemPedido item) {
        dao.salvar(item);
    }

    @Override
    public Optional<ItemPedido> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<ItemPedido> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(ItemPedido item) {
        return dao.atualizar(item);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
