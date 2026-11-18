import dao.BoloDao;
import dao.ClienteDao;
import dao.FuncionarioDao;
import dao.ProdutoDao;
import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        BoloDao boloDao = new BoloDao();
        Bolo bolo1 = new Bolo("Branco, chantinho, chocolate", BigDecimal.valueOf(25.0),"Para aniversário",1,1, TipoMassa.BRANCA, TipoCobertura.CHANTININHO, 1, BigDecimal.valueOf(19.90), TipoRecheio.CHOCOLATE);

        try {
            //SALVAR
            boloDao.salvar(bolo1);

            System.out.println("\nBUSCA POR ID");
            //BUSCAR POR ID
            Optional<Bolo> bolo2 = boloDao.buscarPorId(25);
            System.out.println(bolo2.get().getNome());
            System.out.println(bolo2.get().getTipoMassa().name());

            System.out.println("\nBUSCA POR NOME");
            //BUSCAR POR NOME
            List<Bolo> boloNome = boloDao.buscarPorNome("Branco, chantinho, chocolate");
            for (Bolo b : boloNome) {
                System.out.println(b.getNome());
                System.out.println(b.getTipoMassa());
                System.out.println(b.getTipoCobertura());
                System.out.println(b.getTipoRecheio());
            }

            bolo2.get().setNome("Alex Ferreira");

            //ALTERAR
            boloDao.atualizar(bolo2.get());

            System.out.println("\nBOLO COM NOME ALTERADO");
            // IMPRIMIR ALTERADO
            Optional<Bolo> boloRetornado = boloDao.buscarPorId(bolo2.get().getIdProduto());
            System.out.println(boloRetornado.get().getNome());


            System.out.println("DELETOU?");
            //DELETAR
            System.out.println(boloDao.deletar(bolo2.get().getIdProduto()));


        } finally {
            boloDao.fecharConexao();
        }




//        FuncionarioDao funcionarioDao = new FuncionarioDao();
//        Funcionario func1 = new Funcionario(0,"Alexandre Alves","xandeaf", "123", TipoFuncionario.GERENTE);
//
//        try {
//            //SALVAR
//            funcionarioDao.salvar(func1);
//
//            System.out.println("\nBUSCA POR ID");
//            //BUSCAR POR ID
//            Optional<Funcionario> func2 = funcionarioDao.buscarPorId(4);
//            System.out.println(func2.get().getNomeFuncionario());
//
//            System.out.println("\nBUSCA POR NOME COM 'an'");
//            //BUSCAR POR NOME
//            List<Funcionario> funcNome = funcionarioDao.buscarPorNome("an");
//            for (Funcionario func : funcNome) {
//                System.out.println(func.getNomeFuncionario());
//            }
//
//            func2.get().setNomeFuncionario("Alex Ferreira");
//
//            //ALTERAR
//            funcionarioDao.atualizar(func2.get());
//
//            System.out.println("\nLISTA TODOS COM VALOR ALTERADO");
//            // LISTAR TODOS
//            List<Funcionario> funcionarios = funcionarioDao.listarTodos();
//
//            for (Funcionario func : funcionarios) {
//                System.out.println(func.getNomeFuncionario());
//            }
//
//            System.out.println("DELETOU?");
//            //DELETAR
//            System.out.println(funcionarioDao.deletar(4));
//
//
//        } finally {
//            funcionarioDao.fecharConexao();
//        }





//        ClienteDao clienteDao = new ClienteDao();
//        Cliente c1 = new Cliente(0,"Alexandre Alves","88974954", LocalDate.now());
//
//        try {
//            //SALVAR
//            clienteDao.salvar(c1);
//
//            System.out.println("\nBUSCA POR ID");
//            //BUSCAR POR ID
//            Optional<Cliente> c2 = clienteDao.buscarPorId(11);
//            System.out.println(c2.get().getNome());
//
//            System.out.println("\nBUSCA POR NOME COM 'an'");
//            //BUSCAR POR NOME
//            List<Cliente> clienteNome = clienteDao.buscarPorNome("an");
//            for (Cliente cliente : clienteNome) {
//                System.out.println(cliente.getNome());
//            }
//
//            c2.get().setNome("Alex Ferreira");
//
//            //ALTERAR
//            clienteDao.atualizar(c2.get());
//
//            System.out.println("\nLISTA TODOS COM VALOR ALTERADO");
//            // LISTAR CLIENTES
//            List<Cliente> clientes = clienteDao.listarTodos();
//
//            for (Cliente cliente : clientes) {
//                System.out.println(cliente.getNome());
//            }
//
//            System.out.println("DELETOU?");
//            //DELETAR
//            System.out.println(clienteDao.deletar(11));
//
//
//        } finally {
//            clienteDao.fecharConexao();
//        }
    }
}

