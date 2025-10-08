
package br.upf.projetojfprimefaces.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "log_acesso")
public class LogAcessoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O login de tentativa não pode ser vazio.")
    @Size(max = 50, message = "O login de tentativa deve ter no máximo 50 caracteres.")
    @Column(name = "login_tentativa", nullable = false, length = 50)
    private String loginTentativa;

    @NotNull(message = "A data e hora do acesso não pode ser nula.")
    @Column(name = "data_hora", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataHora = OffsetDateTime.now();

    @NotNull(message = "O status de sucesso não pode ser nulo.")
    @Column(name = "sucesso", nullable = false)
    private Boolean sucesso;

    @Size(max = 45, message = "O IP de acesso deve ter no máximo 45 caracteres.")
    @Column(name = "ip_acesso", length = 45)
    private String ipAcesso;

    public LogAcessoEntity() {
    }

    public LogAcessoEntity(Integer id, String loginTentativa, OffsetDateTime dataHora, Boolean sucesso, String ipAcesso) {
        this.id = id;
        this.loginTentativa = loginTentativa;
        this.dataHora = dataHora;
        this.sucesso = sucesso;
        this.ipAcesso = ipAcesso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginTentativa() {
        return loginTentativa;
    }

    public void setLoginTentativa(String loginTentativa) {
        this.loginTentativa = loginTentativa;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getIpAcesso() {
        return ipAcesso;
    }

    public void setIpAcesso(String ipAcesso) {
        this.ipAcesso = ipAcesso;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LogAcessoEntity other = (LogAcessoEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "LogAcessoEntity{" + "id=" + id + ", loginTentativa=" + loginTentativa + ", dataHora=" + dataHora + ", sucesso=" + sucesso + ", ipAcesso=" + ipAcesso + '}';
    }
}

