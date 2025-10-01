
package br.upf.projetojfprimefaces.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "prontuario")
public class ProntuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "O animal não pode ser nulo.")
    @OneToOne
    @JoinColumn(name = "id_animal", referencedColumnName = "id", nullable = false, unique = true)
    private AnimalEntity animal;

    @Column(name = "data_criacao", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dataCriacao = LocalDate.now();

    public ProntuarioEntity() {
    }

    public ProntuarioEntity(Integer id, AnimalEntity animal, LocalDate dataCriacao) {
        this.id = id;
        this.animal = animal;
        this.dataCriacao = dataCriacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AnimalEntity getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalEntity animal) {
        this.animal = animal;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProntuarioEntity other = (ProntuarioEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ProntuarioEntity{" + "id=" + id + ", animal=" + animal.getNome() + ", dataCriacao=" + dataCriacao + "}";
    }
}

