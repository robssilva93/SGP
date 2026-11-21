package repository;

import model.Cliente;
import dao.ClienteDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepository implements InterfaceRepository<Cliente> {

    private final ClienteDao dao;
    public ClienteRepository(ClienteDao dao) {
        this.dao = dao;
    }
    @Override
    public void salvar(Cliente cliente) {
        dao.salvar(cliente);
    }
    @Override
    public Optional<Cliente> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }
    @Override
    public List<Cliente> listarTodos() {
        return dao.listarTodos();
    }
    @Override
    public boolean atualizar(Cliente cliente) {
        return dao.atualizar(cliente);
    }
    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
