package model;

import java.math.BigDecimal;

public class BemCasado extends Produto{

    private BigDecimal precoUnidade;

    public BemCasado (){}
    public BemCasado(BigDecimal precoUnidade){
        this.precoUnidade = precoUnidade;
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }
}
