package model;

import java.math.BigDecimal;

public class Bolo extends Produto{

    private float diametro;
    private float peso;
    private TipoMassa tipoMassa;
    private TipoCobertura tipoCobertura;
    private int andares;
    private BigDecimal precoDiametro;
    private TipoRecheio tipoRecheio;

    public Bolo(){}
    public Bolo(float diametro, float peso, TipoMassa tipoMassa, TipoCobertura tipoCobertura,
                int andares, BigDecimal precoDiametro, TipoRecheio tipoRecheio) {
        this.diametro = diametro;
        this.peso = peso;
        this.tipoMassa = tipoMassa;
        this.tipoCobertura = tipoCobertura;
        this.andares = andares;
        this.precoDiametro = precoDiametro;
        this.tipoRecheio = tipoRecheio;

    }

    public float getDiametro() {
        return diametro;
    }

    public void setDiametro(float diametro) {
        this.diametro = diametro;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public TipoMassa getTipoMassa() {
        return tipoMassa;
    }

    public void setTipoMassa(TipoMassa tipoMassa) {
        this.tipoMassa = tipoMassa;
    }

    public TipoCobertura getTipoCobertura() {
        return tipoCobertura;
    }

    public void setTipoCobertura(TipoCobertura tipoCobertura) {
        this.tipoCobertura = tipoCobertura;
    }

    public int getAndares() {
        return andares;
    }

    public void setAndares(int andares) {
        this.andares = andares;
    }

    public BigDecimal getPrecoDiametro() {
        return precoDiametro;
    }

    public void setPrecoDiametro(BigDecimal precoDiametro) {
        this.precoDiametro = precoDiametro;
    }

    public TipoRecheio getTipoRecheio() {
        return tipoRecheio;
    }

    public void setTipoRecheio(TipoRecheio tipoRecheio) {
        this.tipoRecheio = tipoRecheio;
    }
}
