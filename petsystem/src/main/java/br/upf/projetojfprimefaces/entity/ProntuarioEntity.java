// src/main/java/br/upf/projetojfprimefaces/entity/ProntuarioEntity.java
package br.upf.projetojfprimefaces.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "prontuario")
public class ProntuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id_animal", referencedColumnName = "id", nullable = false, unique = true)
    private AnimalEntity animal;

    @Temporal(TemporalType.DATE)                 // casa com DATE do SQL
    @Column(name = "data_criacao", nullable = false)
    private Date dataCriacao = new Date();

    @Column(name = "observacoes")
    private String observacoes;

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public AnimalEntity getAnimal() { return animal; }
    public void setAnimal(AnimalEntity animal) { this.animal = animal; }

    public Date getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    @Override public int hashCode() { return Objects.hash(id); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProntuarioEntity)) return false;
        ProntuarioEntity that = (ProntuarioEntity) o;
        return Objects.equals(id, that.id);
    }
}
