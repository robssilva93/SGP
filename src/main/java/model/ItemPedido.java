package model;

import java.math.BigDecimal;

public class ItemPedido {
    private int idItem;
    private Pedido pedido;
    private int quantidade;
    private BigDecimal valorParcial;
    private String observacoes;

    public ItemPedido(){}
    public ItemPedido(int idItem, Pedido pedido, int quantidade, BigDecimal valorParcial, String observacoes) {
        this.idItem = idItem;
        this.pedido = pedido;
        this.quantidade = quantidade;
        this.valorParcial = valorParcial;
        this.observacoes = observacoes;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(BigDecimal valorParcial) {
        this.valorParcial = valorParcial;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
