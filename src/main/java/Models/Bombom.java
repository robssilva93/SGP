package Models;

import java.math.BigDecimal;

public class Bombom extends Produto{
    private Sabor saborBombom;
    private TipoRecheio tipoRecheio;
    private BigDecimal precoUnidade;

    public Bombom() {}
    public Bombom(BigDecimal precoUnidade, Sabor saborBombom, TipoRecheio tipoRecheio) {
        this.precoUnidade = precoUnidade;
        this.saborBombom = saborBombom;
        this.tipoRecheio = tipoRecheio;
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }

    public TipoRecheio getTipoRecheio() {
        return tipoRecheio;
    }

    public void setTipoRecheio(TipoRecheio tipoRecheio) {
        this.tipoRecheio = tipoRecheio;
    }

    public Sabor getSaborBombom() {
        return saborBombom;
    }

    public void setSaborBombom(Sabor saborBombom) {
        this.saborBombom = saborBombom;
    }
}
