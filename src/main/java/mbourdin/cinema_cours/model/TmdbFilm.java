package mbourdin.cinema_cours.model;

import javax.persistence.*;
@Entity
@Table(name="TmdbFilms")
public class TmdbFilm {
    private Long id;
    private String title;


    @Id
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name="title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
