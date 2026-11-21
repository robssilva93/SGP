package repository;

import dao.BoloDao;
import model.Bolo;

import java.util.List;
import java.util.Optional;

public class BoloRepository implements InterfaceRepository<Bolo> {

    private final BoloDao dao;
    public BoloRepository(BoloDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(Bolo bolo) {
        dao.salvar(bolo);
    }

    @Override
    public Optional<Bolo> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<Bolo> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(Bolo bolo) {
        return dao.atualizar(bolo);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
