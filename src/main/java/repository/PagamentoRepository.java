package repository;

import dao.PagamentoDao;
import model.Pagamento;

import java.util.List;
import java.util.Optional;

public class PagamentoRepository implements InterfaceRepository<Pagamento> {

    private final PagamentoDao dao;
    public PagamentoRepository(PagamentoDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(Pagamento pagamento) {
        dao.salvar(pagamento);
    }

    @Override
    public Optional<Pagamento> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<Pagamento> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(Pagamento pagamento) {
        return dao.atualizar(pagamento);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
