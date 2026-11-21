package repository;

import dao.PirulitoChocolateDao;
import model.PirulitoChocolate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PirulitoChocolateRepository implements InterfaceRepository<PirulitoChocolate> {

    private final PirulitoChocolateDao dao;
    public PirulitoChocolateRepository(PirulitoChocolateDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(PirulitoChocolate pirulitoChocolate) {
        dao.salvar(pirulitoChocolate);
    }

    @Override
    public Optional<PirulitoChocolate> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<PirulitoChocolate> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(PirulitoChocolate pirulitoChocolate) {
        return dao.atualizar(pirulitoChocolate);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
