package mbourdin.cinema_cours.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="films")
public class Film {
    //private PersonsDao personsDao;
    private long id;
    String titre;
    String afficheNom;
    double note;
    private String resume;
    private Personne realisateur;
    private List<Play> roles;
    private Set<Review> reviews;
    private Set<Genre> genres;
    private Set<Seance> seances;
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
    @JoinColumn(name = "film_director")
    public Personne getRealisateur(){return realisateur;
    }

    public void setRealisateur(Personne realisateur) {
        this.realisateur = realisateur;
    }


    @OneToMany(mappedBy ="film")
    public Set<Seance> getSeances() {
        return seances;
    }

    public void setSeances(Set<Seance> seances) {
        this.seances = seances;
    }




    @OneToMany(mappedBy = "film",cascade=CascadeType.ALL,orphanRemoval = true)
    public List<Play> getRoles() {
        return roles;
    }

    public void setRoles(List<Play> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "film",cascade=CascadeType.ALL,orphanRemoval = true)
    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    @ManyToMany(mappedBy = "films")
    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
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

}