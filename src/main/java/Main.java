import dao.*;
import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        BemCasadoDao bemCasadoDao = new BemCasadoDao();
        BemCasado bc1 = new BemCasado("bem casado gostoso", BigDecimal.valueOf(6.0), "descrição do bem casado", BigDecimal.valueOf(15.0));

        try {
            //SALVAR
            bemCasadoDao.salvar(bc1);

            System.out.println("\nBUSCA POR ID");
            //BUSCAR POR ID
            Optional<BemCasado> bc2 = bemCasadoDao.buscarPorId(30);
            System.out.println(bc2.get().getNome());
            System.out.println(bc2.get().getDescricao());

            System.out.println("\nBUSCA POR NOME");
            //BUSCAR POR NOME
            List<BemCasado> bcNome = bemCasadoDao.buscarPorNome("bem casado gostoso");
            for (BemCasado bc : bcNome) {
                System.out.println(bc.getNome());
                System.out.println(bc.getDescricao());
            }

            bc2.get().setNome("Alex Ferreira");

            //ALTERAR
            bemCasadoDao.atualizar(bc2.get());

            System.out.println("\nBOMBOM COM NOME ALTERADO");
            // IMPRIMIR ALTERADO
            Optional<BemCasado> bcRetornado = bemCasadoDao.buscarPorId(bc2.get().getIdProduto());
            System.out.println(bcRetornado.get().getNome());


            System.out.println("DELETOU?");
            //DELETAR
            System.out.println(bemCasadoDao.deletar(bc2.get().getIdProduto()));


        } finally {
            bemCasadoDao.fecharConexao();
        }




//        BoloBombomDao boloBombomDao = new BoloBombomDao();
//        BoloBombom bbb1 = new BoloBombom("bolo bombom gostoso", BigDecimal.valueOf(15.0), "descrição do bolo bombom", TipoMassa.BRANCA, TipoRecheio.MORANGO, BigDecimal.valueOf(15.0));
//
//        try {
//            //SALVAR
//            boloBombomDao.salvar(bbb1);
//
//            System.out.println("\nBUSCA POR ID");
//            //BUSCAR POR ID
//            Optional<BoloBombom> bbb2 = boloBombomDao.buscarPorId(29);
//            System.out.println(bbb2.get().getNome());
//            System.out.println(bbb2.get().getTipoMassa().name());
//            System.out.println(bbb2.get().getTipoRecheio().name());
//
//            System.out.println("\nBUSCA POR NOME");
//            //BUSCAR POR NOME
//            List<BoloBombom> bbbNome = boloBombomDao.buscarPorNome("bolo bombom gostoso");
//            for (BoloBombom bbb : bbbNome) {
//                System.out.println(bbb.getNome());
//                System.out.println(bbb.getDescricao());
//            }
//
//            bbb2.get().setNome("Alex Ferreira");
//
//            //ALTERAR
//            boloBombomDao.atualizar(bbb2.get());
//
//            System.out.println("\nBOMBOM COM NOME ALTERADO");
//            // IMPRIMIR ALTERADO
//            Optional<BoloBombom> bbbRetornado = boloBombomDao.buscarPorId(bbb2.get().getIdProduto());
//            System.out.println(bbbRetornado.get().getNome());
//
//
//            System.out.println("DELETOU?");
//            //DELETAR
//            System.out.println(boloBombomDao.deletar(bbb2.get().getIdProduto()));
//
//
//        } finally {
//            boloBombomDao.fecharConexao();
//        }




//        PirulitoChocolateDao pirulitoChocolateDao = new PirulitoChocolateDao();
//        PirulitoChocolate pc1 = new PirulitoChocolate("pirulito chocolate gostoso", BigDecimal.valueOf(10.0), "descrição do pirulito chocolate", BigDecimal.valueOf(10.0), Sabor.CHOCOLATE);
//
//        try {
//            //SALVAR
//            pirulitoChocolateDao.salvar(pc1);
//
//            System.out.println("\nBUSCA POR ID");
//            //BUSCAR POR ID
//            Optional<PirulitoChocolate> pc2 = pirulitoChocolateDao.buscarPorId(28);
//            System.out.println(pc2.get().getNome());
//            System.out.println(pc2.get().getSaborPirulito().name());
//
//            System.out.println("\nBUSCA POR NOME");
//            //BUSCAR POR NOME
//            List<PirulitoChocolate> pcNome = pirulitoChocolateDao.buscarPorNome("pirulito chocolate gostoso");
//            for (PirulitoChocolate pc : pcNome) {
//                System.out.println(pc.getNome());
//                System.out.println(pc.getDescricao());
//            }
//
//            pc2.get().setNome("Alex Ferreira");
//
//            //ALTERAR
//            pirulitoChocolateDao.atualizar(pc2.get());
//
//            System.out.println("\nBOMBOM COM NOME ALTERADO");
//            // IMPRIMIR ALTERADO
//            Optional<PirulitoChocolate> pcRetornado = pirulitoChocolateDao.buscarPorId(pc2.get().getIdProduto());
//            System.out.println(pcRetornado.get().getNome());
//
//
//            System.out.println("DELETOU?");
//            //DELETAR
//            System.out.println(pirulitoChocolateDao.deletar(pc2.get().getIdProduto()));
//
//
//        } finally {
//            pirulitoChocolateDao.fecharConexao();
//        }




//        BombomDao bombomDao = new BombomDao();
//        Bombom bb1 = new Bombom("bombom gostoso", BigDecimal.valueOf(7.0), "descrição do bombom", BigDecimal.valueOf(7.0), Sabor.CHOCOLATE, TipoRecheio.MORANGO);
//
//        try {
//            //SALVAR
//            bombomDao.salvar(bb1);
//
//            System.out.println("\nBUSCA POR ID");
//            //BUSCAR POR ID
//            Optional<Bombom> bb2 = bombomDao.buscarPorId(27);
//            System.out.println(bb2.get().getNome());
//            System.out.println(bb2.get().getSaborBombom().name());
//            System.out.println(bb2.get().getTipoRecheio().name());
//
//            System.out.println("\nBUSCA POR NOME");
//            //BUSCAR POR NOME
//            List<Bombom> bbNome = bombomDao.buscarPorNome("bombom gostoso");
//            for (Bombom bb : bbNome) {
//                System.out.println(bb.getNome());
//                System.out.println(bb.getDescricao());
//            }
//
//            bb2.get().setNome("Alex Ferreira");
//
//            //ALTERAR
//            bombomDao.atualizar(bb2.get());
//
//            System.out.println("\nBOMBOM COM NOME ALTERADO");
//            // IMPRIMIR ALTERADO
//            Optional<Bombom> bbRetornado = bombomDao.buscarPorId(bb2.get().getIdProduto());
//            System.out.println(bbRetornado.get().getNome());
//
//
//            System.out.println("DELETOU?");
//            //DELETAR
//            System.out.println(bombomDao.deletar(bb2.get().getIdProduto()));
//
//
//        } finally {
//            bombomDao.fecharConexao();
//        }



//        BrigadeiroDao brigadeiroDao = new BrigadeiroDao();
//        Brigadeiro brig1 = new Brigadeiro("brigadeiro personalizado coco", BigDecimal.valueOf(2.0), "descrição brigadeiro personalizado coco", true,"Tema escolhido", SaborBrigadeiro.COCO, BigDecimal.valueOf(2.0));
//
//        try {
//            //SALVAR
//            brigadeiroDao.salvar(brig1);
//
//            System.out.println("\nBUSCA POR ID");
//            //BUSCAR POR ID
//            Optional<Brigadeiro> brig2 = brigadeiroDao.buscarPorId(26);
//            System.out.println(brig2.get().getNome());
//            System.out.println(brig2.get().getSaborBrigadeiro().name());
//
//            System.out.println("\nBUSCA POR NOME");
//            //BUSCAR POR NOME
//            List<Brigadeiro> brigNome = brigadeiroDao.buscarPorNome("brigadeiro personalizado coco");
//            for (Brigadeiro b : brigNome) {
//                System.out.println(b.getNome());
//                System.out.println(b.getDescricao());
//                System.out.println(b.getTema());
//            }
//
//            brig2.get().setNome("Alex Ferreira");
//
//            //ALTERAR
//            brigadeiroDao.atualizar(brig2.get());
//
//            System.out.println("\nBRIGADEIRO COM NOME ALTERADO");
//            // IMPRIMIR ALTERADO
//            Optional<Brigadeiro> brigRetornado = brigadeiroDao.buscarPorId(brig2.get().getIdProduto());
//            System.out.println(brigRetornado.get().getNome());
//
//
//            System.out.println("DELETOU?");
//            //DELETAR
//            System.out.println(brigadeiroDao.deletar(brig2.get().getIdProduto()));
//
//
//        } finally {
//            brigadeiroDao.fecharConexao();
//        }




//        BoloDao boloDao = new BoloDao();
//        Bolo bolo1 = new Bolo("Branco, chantinho, chocolate", BigDecimal.valueOf(25.0),"Para aniversário",1,1, TipoMassa.BRANCA, TipoCobertura.CHANTININHO, 1, BigDecimal.valueOf(19.90), TipoRecheio.CHOCOLATE);
//
//        try {
//            //SALVAR
//            boloDao.salvar(bolo1);
//
//            System.out.println("\nBUSCA POR ID");
//            //BUSCAR POR ID
//            Optional<Bolo> bolo2 = boloDao.buscarPorId(25);
//            System.out.println(bolo2.get().getNome());
//            System.out.println(bolo2.get().getTipoMassa().name());
//
//            System.out.println("\nBUSCA POR NOME");
//            //BUSCAR POR NOME
//            List<Bolo> boloNome = boloDao.buscarPorNome("Branco, chantinho, chocolate");
//            for (Bolo b : boloNome) {
//                System.out.println(b.getNome());
//                System.out.println(b.getTipoMassa());
//                System.out.println(b.getTipoCobertura());
//                System.out.println(b.getTipoRecheio());
//            }
//
//            bolo2.get().setNome("Alex Ferreira");
//
//            //ALTERAR
//            boloDao.atualizar(bolo2.get());
//
//            System.out.println("\nBOLO COM NOME ALTERADO");
//            // IMPRIMIR ALTERADO
//            Optional<Bolo> boloRetornado = boloDao.buscarPorId(bolo2.get().getIdProduto());
//            System.out.println(boloRetornado.get().getNome());
//
//
//            System.out.println("DELETOU?");
//            //DELETAR
//            System.out.println(boloDao.deletar(bolo2.get().getIdProduto()));
//
//
//        } finally {
//            boloDao.fecharConexao();
//        }




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

