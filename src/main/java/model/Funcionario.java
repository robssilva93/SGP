package model;

public class Funcionario {

    private int idFuncionario;
    private String nomeFuncionario;
    private String login;
    private String senha;
    private TipoFuncionario cargo;

    public Funcionario(){}

    public Funcionario(int idFuncionario, String nomeFuncionario, String login, String senha, TipoFuncionario cargo) {
        this.idFuncionario = idFuncionario;
        this.nomeFuncionario = nomeFuncionario;
        this.login = login;
        this.senha = senha;
        this.cargo = cargo;
    }
    
    public int getIdFuncionario() {
        return idFuncionario;
    }
    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
    public String getNomeFuncionario() {
        return nomeFuncionario;
    }
    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public TipoFuncionario getCargo() {
        return cargo;
    }
    public void setCargo(TipoFuncionario cargo) {
    }


}
