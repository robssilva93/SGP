package repository;

import dao.BemCasadoDao;
import model.BemCasado;

import java.util.List;
import java.util.Optional;

public class BemCasadoRepository implements InterfaceRepository<BemCasado> {

    private final BemCasadoDao dao;
    public BemCasadoRepository(BemCasadoDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(BemCasado bemCasado) {
        dao.salvar(bemCasado);
    }

    @Override
    public Optional<BemCasado> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<BemCasado> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(BemCasado bemCasado) {
        return dao.atualizar(bemCasado);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
