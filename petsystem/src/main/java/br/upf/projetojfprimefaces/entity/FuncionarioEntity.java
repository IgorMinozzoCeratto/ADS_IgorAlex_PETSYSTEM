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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "funcionario")
public class FuncionarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String senha;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotNull
    @Column(name = "data_contratacao", nullable = false)
    private LocalDate dataContratacao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_perfil", referencedColumnName = "id", nullable = false)
    private PerfilEntity perfil;

    // ----------------------------------------------------------
    // Construtores
    // ----------------------------------------------------------

    public FuncionarioEntity() {
    }

    public FuncionarioEntity(Integer id) {
        this.id = id;
    }

    public FuncionarioEntity(Integer id, String nome, String login, String senha, String email,
                             LocalDate dataContratacao, PerfilEntity perfil) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.dataContratacao = dataContratacao;
        this.perfil = perfil;
    }

    // ----------------------------------------------------------
    // Getters e Setters
    // ----------------------------------------------------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public PerfilEntity getPerfil() { return perfil; }
    public void setPerfil(PerfilEntity perfil) { this.perfil = perfil; }

    // ----------------------------------------------------------
    // Métodos utilitários
    // ----------------------------------------------------------

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FuncionarioEntity other = (FuncionarioEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return nome + " (" + login + ")";
    }
}
