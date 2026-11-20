package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pagamento {
    private int idPagamento;
    private int idPedido;
    private BigDecimal valorPago;
    private LocalDate dataPagamento;
    private String comprovante;

    public Pagamento() {}

    public Pagamento(int idPedido, BigDecimal valorPago, LocalDate dataPagamento, String comprovante) {
        this.idPedido = idPedido;
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

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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
