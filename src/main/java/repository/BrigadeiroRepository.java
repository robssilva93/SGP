package repository;

import dao.BrigadeiroDao;
import model.Brigadeiro;

import java.util.List;
import java.util.Optional;

public class BrigadeiroRepository implements InterfaceRepository<Brigadeiro>{

    private final BrigadeiroDao dao;
    public BrigadeiroRepository(BrigadeiroDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(Brigadeiro brigadeiro) {
        dao.salvar(brigadeiro);
    }

    @Override
    public Optional<Brigadeiro> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<Brigadeiro> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(Brigadeiro brigadeiro) {
        return dao.atualizar(brigadeiro);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
