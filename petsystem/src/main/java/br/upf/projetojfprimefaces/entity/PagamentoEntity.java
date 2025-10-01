
package br.upf.projetojfprimefaces.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "pagamento")
public class PagamentoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "O valor pago não pode ser nulo.")
    @Column(name = "valor_pago", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPago;

    @NotNull(message = "A data de pagamento não pode ser nula.")
    @Column(name = "data_pagamento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataPagamento;

    @NotBlank(message = "A forma de pagamento não pode ser vazia.")
    @Size(max = 50, message = "A forma de pagamento deve ter no máximo 50 caracteres.")
    @Column(name = "forma_pagamento", nullable = false, length = 50)
    private String formaPagamento;

    @NotNull(message = "O tutor não pode ser nulo.")
    @JoinColumn(name = "id_tutor", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private TutorEntity tutor;

    @NotNull(message = "A movimentação financeira não pode ser nula.")
    @JoinColumn(name = "id_movimentacao", referencedColumnName = "id", nullable = false, unique = true)
    @OneToOne(optional = false)
    private FinanceiroMovimentacaoEntity movimentacao;

    @JoinColumn(name = "id_consulta", referencedColumnName = "id")
    @ManyToOne
    private ConsultaEntity consulta;

    public PagamentoEntity() {
    }

    public PagamentoEntity(Integer id, BigDecimal valorPago, Date dataPagamento, String formaPagamento, TutorEntity tutor, FinanceiroMovimentacaoEntity movimentacao) {
        this.id = id;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.tutor = tutor;
        this.movimentacao = movimentacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public TutorEntity getTutor() {
        return tutor;
    }

    public void setTutor(TutorEntity tutor) {
        this.tutor = tutor;
    }

    public FinanceiroMovimentacaoEntity getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(FinanceiroMovimentacaoEntity movimentacao) {
        this.movimentacao = movimentacao;
    }

    public ConsultaEntity getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaEntity consulta) {
        this.consulta = consulta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PagamentoEntity other = (PagamentoEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "PagamentoEntity{" + "id=" + id + ", valorPago=" + valorPago + ", dataPagamento=" + dataPagamento + "}";
    }
}

