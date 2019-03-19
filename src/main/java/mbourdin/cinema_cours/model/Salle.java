package mbourdin.cinema_cours.model;


import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="salle")
public class Salle {
    public class TailleNegativeException extends RuntimeException{}

    private Integer id;
    private Integer taille;
    private Boolean active;
    private Set<Seance> seances;
    private String nom;

    public Salle() {
        active=Boolean.FALSE;
    }
    @Basic
    @Column(name="active",nullable = false)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }



    @Basic
    @Column(name="nom")

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }





    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Basic
    @Column(name="taille")
    public Integer getTaille() {
        return taille;
    }

    public void setTaille(Integer taille) {
        if (taille<0){
            throw new TailleNegativeException();
        }
        else {
            this.taille = taille;
        }
    }
    @OneToMany(mappedBy ="salle")
    public Set<Seance> getSeances() {
        return seances;
    }

    public void setSeances(Set<Seance> seances) {
        this.seances = seances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salle)) return false;
        Salle salle = (Salle) o;
        return getId() == salle.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
