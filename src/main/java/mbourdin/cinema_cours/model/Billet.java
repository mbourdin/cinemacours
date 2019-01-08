package mbourdin.cinema_cours.model;

import mbourdin.cinema_cours.service.Panier;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="billets")
public class Billet implements Comparable{
    private Long id;
    private Seance seance;
    private Commande commande;
    private double prix;

    public Billet()
    {}

    public Billet(Seance seance, Commande commande, double prix) {
        this.seance = seance;
        this.commande = commande;
        this.prix = prix;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Billet) )return 1;
        Billet that=(Billet) o;
        return id.compareTo(that.id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seance_id")
    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="commande_id")
    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    @Basic
    @Column(name="prix")
    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Billet)) return false;
        Billet billet = (Billet) o;
        return getId().equals(billet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "seance "+seance.getId()+" salle "+seance.getSalle().getId()+" "+seance.getFilm().getTitre()+" date"+seance.getDebut().toString()+" prix:"+prix;
    }
}
