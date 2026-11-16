package repository;

import java.util.List;
import java.util.Optional;

public interface InterfaceRepository<T> {

        void  salvar(T objeto);
        Optional<T> buscarPorId(int id);
        List<T> listarTodos();
        boolean atualizar(T objeto);
        boolean deletar(int id);
    }

