
package mbourdin.cinema_cours.model;

import mbourdin.cinema_cours.service.Utilities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="users")
public class Utilisateur {
    static public final int admin=1;
    static public final String ADMIN_RIGHTS="ADMIN";
    static public final int vendeur=2;
    static public final String VENDEUR_RIGHTS="VENDEUR";
    static public final int normal=4;
    static public final String USER_RIGHTS="USER";
    static public final int moderateur=8;
    static public final String MODERATEUR_RIGHTS="MODERATEUR";

    private long id;
    private String nom;
    private String prenom;
    @NotBlank
    private String login;
    @Email
    private String email;
    private String link;
    private String password;
    private int type;//1 = admin ; 2 = vendeur ; 4 = normal;8 = modérateur;
    private Boolean abonne;
    private Set<Review> reviews;
    private Set<Commande> commandes;
    private Set<NewsLetter> newsLetters;
    private boolean actif;

    public boolean hasType(int statut)
    {   if (!Utilities.isPowerOfTwo(statut)) throw new IllegalArgumentException("status invalide en entree de utilisateur.hasStatut");
        else return( (this.type/statut)%2 ==1);
    }
    public Utilisateur()
    {   type=Utilisateur.normal;
        link=null;
        password ="";
        abonne=Boolean.FALSE;
    }
    public Utilisateur(int type)
    {
        this.type=type;
        link=null;
        password ="";
        abonne=Boolean.FALSE;
    }



    @Basic
    @Column(name="actif")
    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
