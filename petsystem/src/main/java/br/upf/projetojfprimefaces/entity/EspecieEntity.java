
package br.upf.projetojfprimefaces.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "especie")
public class EspecieEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome da espécie não pode ser vazio.")
    @Size(max = 100, message = "O nome da espécie deve ter no máximo 100 caracteres.")
    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    @OneToMany(mappedBy = "especie")
    private List<RacaEntity> racas;

    public EspecieEntity() {
    }

    public EspecieEntity(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
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

    public List<RacaEntity> getRacas() {
        return racas;
    }

    public void setRacas(List<RacaEntity> racas) {
        this.racas = racas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EspecieEntity other = (EspecieEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "EspecieEntity{" + "id=" + id + ", nome=" + nome + '}';
    }
}

