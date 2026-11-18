package model;

import java.math.BigDecimal;

public class BemCasado extends Produto{

    private BigDecimal precoUnidade;

    public BemCasado (){}
    public BemCasado(String nome, BigDecimal precoFinal, String descricao, BigDecimal precoUnidade){
        this.nome = nome;
        this.precoFinal = precoFinal;
        this.descricao = descricao;
        this.precoUnidade = precoUnidade;
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }
}
