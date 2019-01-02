package mbourdin.cinema_cours.model;

import mbourdin.cinema_cours.service.Panier;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="billets")
public class Billet {
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
    public int hashCode() {
        return Objects.hashCode(this);
    }
}
