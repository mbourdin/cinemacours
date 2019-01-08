package mbourdin.cinema_cours.model;




import mbourdin.cinema_cours.model.Billet;
import mbourdin.cinema_cours.service.Panier;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="commande")
public class Commande {
    Long id;
    List<Billet> billets;
    LocalDateTime date;
    Boolean paye;
    Utilisateur utilisateur;



    public Commande(Panier panier,Utilisateur utilisateur)
    {   this.utilisateur=utilisateur;
        setBillets(panier.getBillets());
        for(Billet billet : billets)
        {billet.setCommande(this);

        }
        paye=Boolean.FALSE;
        date=LocalDateTime.now();
    }
    public Commande() {
        billets=new ArrayList<Billet>();
        paye=Boolean.FALSE;
        date=LocalDateTime.now();
    }

    public void add(Billet billet)
    {   billets.add(billet);
    }
    public void remove(Billet billet)
    {   billets.remove(billet);
    }

    public double prix()
    {   double res=0.0;
        for(Billet billet : billets)
        {   res+=billet.getPrix();

        }
        return res;
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
    @JoinColumn(name = "person_id")
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @OneToMany(mappedBy = "commande")
    public List<Billet> getBillets() {
        return billets;
    }
    public void setBillets(List<Billet> billets) {
        this.billets = billets;
    }

    @Basic
    @Column(name = "date")
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    @Basic
    @Column(name="paye")
    public Boolean getPaye() {
        return paye;
    }

    public void setPaye(Boolean paye) {
        this.paye = paye;
    }

    public boolean payer()
    {       setPaye(true);
            return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commande)) return false;
        Commande commande = (Commande) o;
        return getId().equals(commande.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBillets(), getDate(), getPaye(), getUtilisateur());
    }
}
