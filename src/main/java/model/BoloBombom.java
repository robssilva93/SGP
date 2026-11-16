package model;

import java.math.BigDecimal;

public class BoloBombom extends Produto{
    private TipoMassa tipoMassa;
    private TipoRecheio tipoRecheio;
    private BigDecimal precoUnidade;

    public BoloBombom() {}
    public BoloBombom( TipoMassa tipoMassa, TipoRecheio tipoRecheio, BigDecimal precoUnidade ) {
        this.tipoMassa = tipoMassa;
        this.tipoRecheio = tipoRecheio;
        this.precoUnidade = precoUnidade;
    }

    public TipoMassa getTipoMassa() {
        return tipoMassa;
    }

    public void setTipoMassa(TipoMassa tipoMassa) {
        this.tipoMassa = tipoMassa;
    }

    public TipoRecheio getTipoRecheio() {
        return tipoRecheio;
    }

    public void setTipoRecheio(TipoRecheio tipoRecheio) {
        this.tipoRecheio = tipoRecheio;
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }
}
