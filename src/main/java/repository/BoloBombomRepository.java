package repository;

import model.BoloBombom;

import java.util.List;
import java.util.Optional;

public class BoloBombomRepository implements InterfaceRepository<BoloBombom> {

    private final BoloBombomRepository dao;
    public BoloBombomRepository(BoloBombomRepository dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(BoloBombom boloBombom) {
        dao.salvar(boloBombom);
    }

    @Override
    public Optional<BoloBombom> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<BoloBombom> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(BoloBombom boloBombom) {
        return dao.atualizar(boloBombom);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
