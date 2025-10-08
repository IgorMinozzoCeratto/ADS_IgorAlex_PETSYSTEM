
package br.upf.projetojfprimefaces.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "financeiro_movimentacao")
public class FinanceiroMovimentacaoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "A descrição não pode ser vazia.")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @NotNull(message = "O valor não pode ser nulo.")
    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotBlank(message = "O tipo de movimentação não pode ser vazio.")
    @Size(max = 1, message = "O tipo de movimentação deve ser 'R' ou 'D'.")
    @Column(name = "tipo", nullable = false, length = 1)
    private String tipo; // 'R' para Receita, 'D' para Despesa

    @NotNull(message = "A data de movimentação não pode ser nula.")
    @Column(name = "data_movimentacao", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataMovimentacao = OffsetDateTime.now();

    @NotNull(message = "O funcionário responsável não pode ser nulo.")
    @JoinColumn(name = "id_funcionario_responsavel", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private FuncionarioEntity funcionarioResponsavel;

    public FinanceiroMovimentacaoEntity() {
    }

    public FinanceiroMovimentacaoEntity(Integer id, String descricao, BigDecimal valor, String tipo, OffsetDateTime dataMovimentacao, FuncionarioEntity funcionarioResponsavel) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.dataMovimentacao = dataMovimentacao;
        this.funcionarioResponsavel = funcionarioResponsavel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public OffsetDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(OffsetDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public FuncionarioEntity getFuncionarioResponsavel() {
        return funcionarioResponsavel;
    }

    public void setFuncionarioResponsavel(FuncionarioEntity funcionarioResponsavel) {
        this.funcionarioResponsavel = funcionarioResponsavel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FinanceiroMovimentacaoEntity other = (FinanceiroMovimentacaoEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "FinanceiroMovimentacaoEntity{" + "id=" + id + ", descricao=" + descricao + ", valor=" + valor + ", tipo=" + tipo + "}";
    }
}

