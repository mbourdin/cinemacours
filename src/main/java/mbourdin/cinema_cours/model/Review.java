package mbourdin.cinema_cours.model;

import mbourdin.cinema_cours.web.SeanceController;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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
    public static final int NEW =1;
    public static final int PUBLISHED =2;
    public static final int TO_BE_MODIFIED =3;
    public static final int ABANDONNED =4;
    public static final int REJECTED =5;
    public static final int DELETED =6;

    private static final Map<Integer, List<Integer>> transitionsTo;

    static {
        transitionsTo = new HashMap<>();
        transitionsTo.put(Review.NEW,
                Arrays.asList(new Integer[]{
                        Review.PUBLISHED,
                        Review.NEW,
                        Review.TO_BE_MODIFIED,
                        Review.REJECTED
                }));
        transitionsTo.put(Review.PUBLISHED,
                Arrays.asList(new Integer[]{
                        Review.NEW,
                        Review.DELETED
                }));

        transitionsTo.put(Review.TO_BE_MODIFIED,
                Arrays.asList(new Integer[]{
                        Review.NEW,
                        Review.ABANDONNED
                }));
        transitionsTo.put(Review.ABANDONNED, new ArrayList<>());
        transitionsTo.put(Review.DELETED, new ArrayList<>());
        transitionsTo.put(Review.REJECTED, new ArrayList<>());

    }

    private boolean canTransitTo(int targetState) {

        //return Review.transitions.get(targetState).contains(this.getState());

        return Review.transitionsTo.get(etat).contains(targetState);
    }

    private void transitTo(int target) throws IllegalTransitionException {
        if (canTransitTo(target)) {
            etat = target;
        } else {
            throw new IllegalTransitionException("Transition non autorisée");
        }
    }

    private static final String[] etatStrings={"nouveau","publié","à modifier","abandonné","rejeté","supprimé"};

    public String etatString()

    {   return etatStrings[etat-1];
    }

    public Review(Film film, Utilisateur utilisateur) {
        this.film = film;
        this.utilisateur = utilisateur;
        date=LocalDateTime.now();
        etat= NEW;
    }

    public Review() {
        etat= NEW;
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

    public void validByModerator() throws IllegalTransitionException{
        transitTo(PUBLISHED);

    }

    public void deleteByUser() throws IllegalTransitionException{
        transitTo(DELETED);
    }

    public void keepForEdit() throws IllegalTransitionException{
        transitTo(TO_BE_MODIFIED);

    }

    public void reject() throws IllegalTransitionException{
        transitTo(REJECTED);
    }

    public void abandon() throws IllegalTransitionException{
        transitTo(ABANDONNED);
    }

    public void edit() throws IllegalTransitionException{
        transitTo(NEW);
    }

    public boolean editable() {
        return (etat == NEW || etat == TO_BE_MODIFIED || etat == PUBLISHED);
    }

    public boolean nouveau() {
        return etat == NEW;
    }

    public boolean publie() {
        return etat == PUBLISHED;
    }
}
