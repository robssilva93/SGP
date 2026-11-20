package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private int idFuncionario;
    private LocalDate dataPedido;
    private LocalDate dataAgendada;
    private LocalTime horaAgendada;
    private BigDecimal valorTotal;
    private BigDecimal valorPago;
    private StatusPedido status;
    private TipoAtendimento tipoAtendimento;
    private String endereco;
    private String detalhesEspecificos;
    private List<ItemPedido> itensPedido;
    private List<Pagamento> pagamentos;

    public Pedido() {}

    public Pedido(int idCliente, int idFuncionario, LocalDate dataPedido, LocalDate dataAgendada, LocalTime horaAgendada, BigDecimal valorTotal, BigDecimal valorPago, StatusPedido status, TipoAtendimento tipoAtendimento, String endereco, String detalhesEspecificos, List<ItemPedido> itensPedido, List<Pagamento> pagamentos) {
        this.idCliente = idCliente;
        this.idFuncionario = idFuncionario;
        this.dataPedido = dataPedido;
        this.dataAgendada = dataAgendada;
        this.horaAgendada = horaAgendada;
        this.valorTotal = valorTotal;
        this.valorPago = valorPago;
        this.status = status;
        this.tipoAtendimento = tipoAtendimento;
        this.endereco = endereco;
        this.detalhesEspecificos = detalhesEspecificos;
        this.itensPedido = itensPedido;
        this.pagamentos = pagamentos;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDate getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(LocalDate dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    public LocalTime getHoraAgendada() {
        return horaAgendada;
    }

    public void setHoraAgendada(LocalTime horaAgendada) {
        this.horaAgendada = horaAgendada;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public TipoAtendimento getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(TipoAtendimento tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDetalhesEspecificos() {
        return detalhesEspecificos;
    }

    public void setDetalhesEspecificos(String detalhesEspecificos) {
        this.detalhesEspecificos = detalhesEspecificos;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
}

