package model;

import java.math.BigDecimal;

public class ItemPedido {
    private int idItem;
    private int idPedido;
    private int idProduto;
    private int quantidade;
    private BigDecimal valorParcial;
    private String observacoes;

    public ItemPedido(){}
    public ItemPedido(int idPedido, int idProduto, int quantidade, BigDecimal valorParcial, String observacoes) {
        this.idPedido = idPedido;
        this.idProduto = idProduto;
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

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
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
