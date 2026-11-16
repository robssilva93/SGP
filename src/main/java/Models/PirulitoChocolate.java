package Models;

import java.math.BigDecimal;

public class PirulitoChocolate extends Produto{
    private Sabor saborPirulito;
    private BigDecimal precoUnidade;

    public PirulitoChocolate() {}
    public PirulitoChocolate(BigDecimal precoUnidade, Sabor saborPirulito) {
        this.precoUnidade = precoUnidade;
        this.saborPirulito = saborPirulito;

    }

    public Sabor getSaborPirulito() {
        return saborPirulito;
    }

    public void setSaborPirulito(Sabor saborPirulito) {
        this.saborPirulito = saborPirulito;
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }
}
