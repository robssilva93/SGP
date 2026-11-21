package repository;

import dao.BombomDao;
import model.Bombom;

import java.util.List;
import java.util.Optional;

public class BombomRepository implements InterfaceRepository<Bombom> {

    private final BombomDao dao;
    public BombomRepository(BombomDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(Bombom bombom) {
        dao.salvar(bombom);
    }

    @Override
    public Optional<Bombom> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<Bombom> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(Bombom bombom) {
        return dao.atualizar(bombom);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
