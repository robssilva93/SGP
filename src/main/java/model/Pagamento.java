package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pagamento {
    private int idPagamento;
    private Pedido pedido;
    private BigDecimal valorPago;
    private LocalDate dataPagamento;
    private String comprovante;

    public Pagamento() {}

    public Pagamento(int idPagamento, Pedido pedido, BigDecimal valorPago, LocalDate dataPagamento, String comprovante) {
        this.idPagamento = idPagamento;
        this.pedido = pedido;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
        this.comprovante = comprovante;
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getComprovante() {
        return comprovante;
    }

    public void setComprovante(String comprovante) {
        this.comprovante = comprovante;
    }
}
