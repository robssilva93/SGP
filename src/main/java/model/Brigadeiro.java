package model;

import java.math.BigDecimal;

public class Brigadeiro extends Produto{
    private boolean personalizado;
    private String tema;
    private SaborBrigadeiro saborBrigadeiro;
    private BigDecimal precoUnidade;

    public Brigadeiro() {}

    public Brigadeiro(boolean personalizado, String tema, SaborBrigadeiro saborBrigadeiro, BigDecimal precoUnidade) {
        this.personalizado = personalizado;

        if (this.personalizado) {
            this.tema=tema;
        }else{this.tema = null;}

        if (this.personalizado) {
            this.saborBrigadeiro = SaborBrigadeiro.LEITE_NINHO;
        }else{ this.saborBrigadeiro = saborBrigadeiro;}

        this.precoUnidade = precoUnidade;

    }
    public boolean isPersonalizado() {
        return personalizado;
    }
    public void setPersonalizado(boolean personalizado) {
        this.personalizado = personalizado;
    }
    public String getTema() {
        return tema;
    }
    public void setTema(String tema) {
        this.tema = tema;
    }
    public SaborBrigadeiro getSaborBrigadeiro() {
        return saborBrigadeiro;
    }
    public void setSaborBrigadeiro(SaborBrigadeiro saborBrigadeiro) {
        this.saborBrigadeiro = saborBrigadeiro;
    }
    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }
    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }
}
