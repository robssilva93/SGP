package model;

import java.math.BigDecimal;

public class Produto {

    protected int idProduto;
    protected String nome;
    protected String descricao;
    protected BigDecimal precoFinal;

    public Produto(){}
    public Produto(int idProduto, String nome, String descricao, BigDecimal precoFinal) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.precoFinal = precoFinal;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoFinal() {
        return precoFinal;
    }

    public void setPrecoFinal(BigDecimal precoFinal) {
        this.precoFinal = precoFinal;
    }
}
