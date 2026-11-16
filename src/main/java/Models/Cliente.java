package Models;

import java.time.LocalDate;

public class Cliente {

    private int idCliente;
    private String nome;
    private String telefone;
    private LocalDate dataCadastro;

    public Cliente() {}

    public Cliente(int idCliente, String nome, String telefone, LocalDate dataCadastro) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
