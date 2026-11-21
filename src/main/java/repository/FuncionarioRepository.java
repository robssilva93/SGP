package repository;

import dao.FuncionarioDao;
import model.Funcionario;

import java.util.List;
import java.util.Optional;

public class FuncionarioRepository implements InterfaceRepository<Funcionario> {

    private final FuncionarioDao dao;
    public FuncionarioRepository(FuncionarioDao dao) {
        this.dao = dao;
    }

    @Override
    public void salvar(Funcionario funcionario) {
        dao.salvar(funcionario);
    }

    @Override
    public Optional<Funcionario> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<Funcionario> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public boolean atualizar(Funcionario funcionario) {
        return dao.atualizar(funcionario);
    }

    @Override
    public boolean deletar(int id) {
        return dao.deletar(id);
    }
}
