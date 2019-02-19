package mbourdin.cinema_cours.model;

import mbourdin.cinema_cours.web.SeanceController;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="review")
public class Review {
    private Long id;
    private Film film;
    private Utilisateur utilisateur;
    private String article;
    private LocalDateTime date;
    private int etat;
    //etats possibles
    public static final int NOUVEAU=1;
    public static final int PUBLIE=2;
    public static final int AMODIFIER=3;
    public static final int ABANDONNE=4;
    public static final int REJETE=5;
    public static final int SUPPRIME=6;
    private static final String[] etatStrings={"nouveau","publié","à modifier","abandonné","rejeté","supprimé"};

    public String etatString()
    {   return etatStrings[etat-1];
    }

    public Review(Film film, Utilisateur utilisateur) {
        this.film = film;
        this.utilisateur = utilisateur;
        date=LocalDateTime.now();
        etat=NOUVEAU;
    }

    public Review() {
        etat=NOUVEAU;
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
    @ManyToOne
    @JoinColumn(name = "film_id")
    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
    @ManyToOne
    @JoinColumn(name = "user_id")
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    @Basic
    @Column(name = "article", nullable = false)
    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
    @Basic
    @Column(name = "datte", nullable = false)
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Basic
    @Column(name="etat",nullable = false,columnDefinition="INTEGER default '0'")
    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review review = (Review) o;
        return getId().equals(review.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public String formattedDate() {
        return date.format(SeanceController.formatter);
    }

    public void publier() throws IllegalTransitionException{
        if (etat == NOUVEAU) etat = PUBLIE;
        else throw new IllegalTransitionException("error in review.publier() : transition interdite");
    }

    public void supprimer() throws IllegalTransitionException{
        if (etat == PUBLIE) etat = SUPPRIME;
        else throw new IllegalTransitionException("error in review.publier() : transition interdite");
    }

    public void retenirPourModif() throws IllegalTransitionException{
        if (etat == NOUVEAU) etat = AMODIFIER;
        else throw new IllegalTransitionException("error in review.publier() : transition interdite");
    }

    public void rejeter() throws IllegalTransitionException{
        if (etat == NOUVEAU) etat = REJETE;
        else throw new IllegalTransitionException("error in review.publier() : transition interdite");
    }

    public void abandonner() throws IllegalTransitionException{
        if (etat == AMODIFIER) etat = ABANDONNE;
        else throw new IllegalTransitionException("error in review.publier() : transition interdite");
    }

    public void editer() throws IllegalTransitionException{
        if (editable()) etat = NOUVEAU;
        else throw new IllegalTransitionException("error in review.publier() : transition interdite");
    }

    public boolean editable() {
        return (etat == NOUVEAU || etat == AMODIFIER || etat == PUBLIE);
    }

    public boolean nouveau() {
        return etat == NOUVEAU;
    }

    public boolean publie() {
        return etat == PUBLIE;
    }
}
