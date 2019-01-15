package mbourdin.cinema_cours.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="genres")
public class Genre {

    private long id;
    private String name;
    private Set<Film> films;
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
    @Column(name = "name", nullable = false, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }
    public void addFilm(Film film)
    {   if(!films.contains(film)) {
            films.add(film);
            if (!film.getGenres().contains(this)) {
                film.addGenre(this);
            }
        }
    }
    public void removeFilm(Film film)
    {   if(films.contains(film)) {
        films.remove(film);
        if (film.getGenres().contains(this)) {
            film.removeGenre(this);
        }
    }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return getId() == genre.getId() &&
                getName().equals(genre.getName()) &&
                Objects.equals(getFilms(), genre.getFilms());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getFilms());
    }
}
