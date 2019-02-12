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
    private Boolean valide;

    public Review(Film film, Utilisateur utilisateur) {
        this.film = film;
        this.utilisateur = utilisateur;
        date=LocalDateTime.now();
        valide=false;
    }

    public Review() {
        valide=false;
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
    @Column(name="valide")
    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
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

    public String formattedDate()
    {
        return date.format(SeanceController.formatter);
    }
}
