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

@Entity
@Table(name = "exame")
public class ExameEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "tipo_exame", nullable = false, length = 100)
    private String tipoExame;

    @Basic(optional = false)
    @NotNull
    @Column(name = "data_exame", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataExame;

    @Column(name = "resultado", columnDefinition = "TEXT")
    private String resultado;

    // >>> ADICIONADO: observacoes <<<
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Size(max = 255)
    @Column(name = "documento_anexo_url", length = 255)
    private String documentoAnexoUrl;

    @NotNull(message = "O prontuário não pode ser nulo.")
    @JoinColumn(name = "id_prontuario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ProntuarioEntity prontuario;

    @NotNull(message = "O veterinário não pode ser nulo.")
    @JoinColumn(name = "id_veterinario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private FuncionarioEntity veterinario;

    public ExameEntity() {}
    public ExameEntity(Integer id) { this.id = id; }
    public ExameEntity(Integer id, String tipoExame, Date dataExame, ProntuarioEntity prontuario, FuncionarioEntity veterinario) {
        this.id = id;
        this.tipoExame = tipoExame;
        this.dataExame = dataExame;
        this.prontuario = prontuario;
        this.veterinario = veterinario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipoExame() { return tipoExame; }
    public void setTipoExame(String tipoExame) { this.tipoExame = tipoExame; }

    public Date getDataExame() { return dataExame; }
    public void setDataExame(Date dataExame) { this.dataExame = dataExame; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    // >>> GET/SET observacoes <<<
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getDocumentoAnexoUrl() { return documentoAnexoUrl; }
    public void setDocumentoAnexoUrl(String documentoAnexoUrl) { this.documentoAnexoUrl = documentoAnexoUrl; }

    public ProntuarioEntity getProntuario() { return prontuario; }
    public void setProntuario(ProntuarioEntity prontuario) { this.prontuario = prontuario; }

    public FuncionarioEntity getVeterinario() { return veterinario; }
    public void setVeterinario(FuncionarioEntity veterinario) { this.veterinario = veterinario; }

    @Override
    public int hashCode() { return (id != null ? id.hashCode() : 0); }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExameEntity)) return false;
        ExameEntity other = (ExameEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) return false;
        return true;
    }

    @Override
    public String toString() { return "ExameEntity[ id=" + id + " ]"; }
}
