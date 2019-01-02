package mbourdin.cinema_cours.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="salle")
public class Salle {
    public class TailleNegativeException extends RuntimeException{}

    private int id;
    private int taille;
    private Set<Seance> seances;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Basic
    @Column(name="taille")
    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
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
}
