
package br.upf.projetojfprimefaces.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "raca", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nome", "id_especie"})
})
public class RacaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome da raça não pode ser vazio.")
    @Size(max = 100, message = "O nome da raça deve ter no máximo 100 caracteres.")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotNull(message = "A espécie não pode ser nula.")
    @JoinColumn(name = "id_especie", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private EspecieEntity especie;

    public RacaEntity() {
    }

    public RacaEntity(Integer id, String nome, EspecieEntity especie) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
    }

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

    public EspecieEntity getEspecie() {
        return especie;
    }

    public void setEspecie(EspecieEntity especie) {
        this.especie = especie;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RacaEntity other = (RacaEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "RacaEntity{" + "id=" + id + ", nome=" + nome + ", especie=" + especie.getNome() + "}";
    }
}

