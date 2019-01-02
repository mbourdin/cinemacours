package mbourdin.cinema_cours.model;




import mbourdin.cinema_cours.model.Billet;
import mbourdin.cinema_cours.service.Panier;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name="commande")
public class Commande {
    Long id;
    Set<Billet> billets;
    LocalDateTime date;
    Boolean paye;
    Utilisateur utilisateur;



    public Commande(Panier panier,Utilisateur utilisateur)
    {   this.utilisateur=utilisateur;
        setBillets(panier.getBillets());
        paye=Boolean.FALSE;
        date=LocalDateTime.now();
    }
    public Commande() {
        billets=new HashSet<Billet>();
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
    public Set<Billet> getBillets() {
        return billets;
    }
    public void setBillets(Set<Billet> billets) {
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
}
