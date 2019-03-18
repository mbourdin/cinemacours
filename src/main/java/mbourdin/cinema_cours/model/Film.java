package mbourdin.cinema_cours.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import mbourdin.cinema_cours.web.FilmController;
import mbourdin.cinema_cours.web.SeanceController;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="films")
public class Film {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    private Long id;
    private Long tmdbId;
    private String titre;
    private String titreOriginal;
    private String afficheNom;
    private double note;
    private String resume;
    private Personne realisateur;
    private List<Play> roles;
    private Set<Review> reviews;
    private Set<Genre> genres;
    private Set<Seance> seances;
    private LocalDate annee;
    private Integer duree;// duree en minutes


    public Film() {
        annee=LocalDate.now();
        titre="";
    }
    @Basic
    @Column(name="titreOriginal")
    public String getTitreOriginal() {
        return titreOriginal;
    }

    public void setTitreOriginal(String titreOriginal) {
        this.titreOriginal = titreOriginal;
    }

    @Basic
    @Unique
    @Column(name="tmbdId")
    public Long getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Long tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Basic
    @Column(name="duree")
    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
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

    @Basic
    @Column(name = "title", nullable = false, length = 50)
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Basic
    @Column(name = "rating", nullable = true)
    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    @Basic
    @Column(name = "summary", nullable = true,length= 65535)
    public String getResume() {
        return resume;
    }
    public void setResume(String resume){this.resume=resume;}


    @Basic
    @Column(name = "image_path", nullable = true, length = 80)
    public String getAfficheNom() {
        return afficheNom;
    }
    public void setAfficheNom(String afficheNom){this.afficheNom=afficheNom;}

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "film_director")
    public Personne getRealisateur(){return realisateur;
    }

    public void setRealisateur(Personne realisateur) {
        this.realisateur = realisateur;
    }


    @OneToMany(mappedBy ="film")
    @JsonIgnore
    public Set<Seance> getSeances() {
        return seances;
    }

    public void setSeances(Set<Seance> seances) {
        this.seances = seances;
    }




    @OneToMany(mappedBy = "film",cascade={CascadeType.MERGE,CascadeType.REMOVE})
    @JsonIgnore
    public List<Play> getRoles() {
        return roles;
    }

    public void setRoles(List<Play> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "film",cascade=CascadeType.MERGE)
    @JsonIgnore
    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }


    @ManyToMany(cascade ={ CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "film_genre",
            joinColumns = @JoinColumn(name = "film_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id",
                    referencedColumnName = "id"))
    @JsonIgnore
    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Basic
    @Column(name="release_date")
        public LocalDate getAnnee() {
        return annee;
    }

    public void setAnnee(LocalDate annee) {
        this.annee = annee;
    }

    public boolean addPlay(Play role)
    {   if (!roles.contains(role))
        {   roles.add(role);
            return true;

        }
        return false;

    }

    public void deletePlay(Play role)
    {   roles.remove(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getId() == film.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void addGenre(Genre genre)
    {   if(!genres.contains(genre))
        {
            genres.add(genre);
            if (!genre.getFilms().contains(this)) {
                genre.addFilm(this);
            }
        }

    }
    public void removeGenre(Genre genre)
    {   if(genres.contains(genre))
        {
            genres.remove(genre);
            if (genre.getFilms().contains(this)) {
                genre.removeFilm(this);
            }
        }
    }

    public String formattedDate()
    {   if(annee!=null)
        return annee.format(formatter);
        else return "";
    }

    @Override
    public String toString()
    {   return "tmdbid="+tmdbId+" titre original="+titreOriginal;
    }
}