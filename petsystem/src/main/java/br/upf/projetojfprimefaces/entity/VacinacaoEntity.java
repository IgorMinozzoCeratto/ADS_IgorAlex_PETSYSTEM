
package br.upf.projetojfprimefaces.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "vacinacao")
public class VacinacaoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "tipo_vacina", nullable = false, length = 100)
    private String tipoVacina;

    @Basic(optional = false)
    @NotNull
    @Column(name = "data_aplicacao", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataAplicacao;

    @Size(max = 50)
    @Column(name = "lote", length = 50)
    private String lote;

    @NotNull(message = "O prontuário não pode ser nulo.")
    @JoinColumn(name = "id_prontuario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ProntuarioEntity prontuario;

    @NotNull(message = "O funcionário aplicador não pode ser nulo.")
    @JoinColumn(name = "id_funcionario_aplicador", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private FuncionarioEntity funcionarioAplicador;

    public VacinacaoEntity() {
    }

    public VacinacaoEntity(Integer id) {
        this.id = id;
    }

    public VacinacaoEntity(Integer id, String tipoVacina, Date dataAplicacao, ProntuarioEntity prontuario, FuncionarioEntity funcionarioAplicador) {
        this.id = id;
        this.tipoVacina = tipoVacina;
        this.dataAplicacao = dataAplicacao;
        this.prontuario = prontuario;
        this.funcionarioAplicador = funcionarioAplicador;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoVacina() {
        return tipoVacina;
    }

    public void setTipoVacina(String tipoVacina) {
        this.tipoVacina = tipoVacina;
    }

    public Date getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(Date dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public ProntuarioEntity getProntuario() {
        return prontuario;
    }

    public void setProntuario(ProntuarioEntity prontuario) {
        this.prontuario = prontuario;
    }

    public FuncionarioEntity getFuncionarioAplicador() {
        return funcionarioAplicador;
    }

    public void setFuncionarioAplicador(FuncionarioEntity funcionarioAplicador) {
        this.funcionarioAplicador = funcionarioAplicador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VacinacaoEntity)) {
            return false;
        }
        VacinacaoEntity other = (VacinacaoEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VacinacaoEntity[ id=" + id + " ]";
    }
}

