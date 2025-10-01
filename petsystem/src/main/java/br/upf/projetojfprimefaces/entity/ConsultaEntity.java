
package br.upf.projetojfprimefaces.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "consulta")
public class ConsultaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "A data de agendamento não pode ser nula.")
    @Column(name = "data_agendamento", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataAgendamento;

    @Column(name = "observacoes_clinicas", columnDefinition = "TEXT")
    private String observacoesClinicas;

    @Column(name = "realizada")
    private Boolean realizada = false;

    @NotNull(message = "O prontuário não pode ser nulo.")
    @JoinColumn(name = "id_prontuario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ProntuarioEntity prontuario;

    @NotNull(message = "O veterinário não pode ser nulo.")
    @JoinColumn(name = "id_veterinario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private FuncionarioEntity veterinario;

    public ConsultaEntity() {
    }

    public ConsultaEntity(Integer id) {
        this.id = id;
    }

    public ConsultaEntity(Integer id, OffsetDateTime dataAgendamento, ProntuarioEntity prontuario, FuncionarioEntity veterinario) {
        this.id = id;
        this.dataAgendamento = dataAgendamento;
        this.prontuario = prontuario;
        this.veterinario = veterinario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OffsetDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(OffsetDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getObservacoesClinicas() {
        return observacoesClinicas;
    }

    public void setObservacoesClinicas(String observacoesClinicas) {
        this.observacoesClinicas = observacoesClinicas;
    }

    public Boolean getRealizada() {
        return realizada;
    }

    public void setRealizada(Boolean realizada) {
        this.realizada = realizada;
    }

    public ProntuarioEntity getProntuario() {
        return prontuario;
    }

    public void setProntuario(ProntuarioEntity prontuario) {
        this.prontuario = prontuario;
    }

    public FuncionarioEntity getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(FuncionarioEntity veterinario) {
        this.veterinario = veterinario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConsultaEntity)) {
            return false;
        }
        ConsultaEntity other = (ConsultaEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConsultaEntity[ id=" + id + " ]";
    }
}

