package br.upf.projetojfprimefaces.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "perfil")
public class PerfilEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- Perfis fixos do sistema ---
    public static final int ADMINISTRADOR_ID = 1;
    public static final int VETERINARIO_ID = 2;
    public static final int RECEPCIONISTA_ID = 3;
    public static final int CLIENTE_ID = 4;

    // --- Atributos ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nome", nullable = false, unique = true, length = 50)
    private String nome;

    @Size(max = 255)
    @Column(name = "descricao", length = 255)
    private String descricao;

    @OneToMany(mappedBy = "perfil")
    private List<FuncionarioEntity> funcionarios;

    // --- Construtores ---
    public PerfilEntity() {}

    public PerfilEntity(Integer id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    // --- Getters e Setters ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<FuncionarioEntity> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<FuncionarioEntity> funcionarios) {
        this.funcionarios = funcionarios;
    }

    // --- Métodos utilitários ---
    public static PerfilEntity criarAdministrador() {
        return new PerfilEntity(ADMINISTRADOR_ID, "Administrador", "Acesso total ao sistema.");
    }

    public static PerfilEntity criarVeterinario() {
        return new PerfilEntity(VETERINARIO_ID, "Veterinário", "Responsável por consultas e exames.");
    }

    public static PerfilEntity criarRecepcionista() {
        return new PerfilEntity(RECEPCIONISTA_ID, "Recepcionista", "Responsável por agendamentos e cadastros.");
    }

    public static PerfilEntity criarCliente() {
        return new PerfilEntity(CLIENTE_ID, "Cliente", "Acesso restrito a informações dos próprios animais.");
    }

    // --- hashCode, equals e toString ---
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PerfilEntity other = (PerfilEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return nome;
    }
}
