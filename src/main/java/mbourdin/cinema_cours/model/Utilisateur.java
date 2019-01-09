
package mbourdin.cinema_cours.model;

import mbourdin.cinema_cours.service.Utilities;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="users")
public class Utilisateur {
    static public final int admin=1;
    static public final int vendeur=2;
    static public final int normal=3;

    private long id;
    private String nom;
    private String prenom;
    private String login;
    private String email;
    private String link;
    private String hpw;
    private int type;//1 = admin ; 2 = vendeur ; 3 = normal
    private Boolean abonne;
    private Set<Review> reviews;
    private Set<Commande> commandes;
    private Set<NewsLetter> newsLetters;


    public Utilisateur()
    {   type=Utilisateur.normal;
        link=null;
        hpw="";
        abonne=Boolean.FALSE;
    }
    public void generateLink()
    {   link= Utilities.get_SHA_256_SecurePassword(Calendar.getInstance().getTimeInMillis()+"",id+"");
    }

    @Basic
    @Column(name="surname")
    public String getNom()
    {   return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    @Basic
    @Column(name = "givenname")
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Basic
    @Column(name="password")
    public String getHpw() {
        return hpw;
    }

    public void setHpw(String hpw) {
        this.hpw = hpw;
    }


    @Basic
    @Column(name = "link",nullable=true,length = 80)
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "login", nullable = false, length = 75)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    @Basic
    @Column(name = "email", nullable = false, length = 30)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Basic
    @Column(name = "type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    @Override
    public String toString()
    {   String s=id+","+login+","+nom+","+prenom+","+email+",type:"+type+",abonnement:"+abonne;
        return s;

    }

    @OneToMany(mappedBy = "utilisateur")
    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }


    @OneToMany(mappedBy = "utilisateur")
    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    @Basic
    @Column(name = "abonne",nullable = false)
    public Boolean getAbonne() {
        return abonne;
    }

    public void setAbonne(Boolean abonne) {
        this.abonne = abonne;
    }




    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if (! (obj instanceof Utilisateur)) return false;
        if (((Utilisateur) obj).id==id) return true;
        return false;
    }

    @ManyToMany(mappedBy = "utilisateurs")
    public Set<NewsLetter> getNewsLetters() {
        return newsLetters;
    }

    public void setNewsLetters(Set<NewsLetter> newsLetters) {
        this.newsLetters = newsLetters;
    }

    public void addNewsLetter(NewsLetter newsLetter)
    {   if(!newsLetters.contains(newsLetter))
    {
        newsLetters.add(newsLetter);
        if (!newsLetter.getUtilisateurs().contains(this)) {
            newsLetter.addUtilisater(this);
        }
    }

    }
    public void removeNewsletter(NewsLetter newsLetter)
    {   if(newsLetters.contains(newsLetter))
        {
            newsLetters.remove(newsLetter);
            if (newsLetter.getUtilisateurs().contains(this)) {
                newsLetter.removeUtilisateur(this);
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
