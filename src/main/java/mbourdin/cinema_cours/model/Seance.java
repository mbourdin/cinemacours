package mbourdin.cinema_cours.model;


import mbourdin.cinema_cours.service.SeanceChamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="seance")
public class Seance {
    public static final double prixdefaut=9.00;
    private Long id;
    private Film film;
    private Salle salle;
    private LocalDateTime debut;
    private Set<Billet> billets;

    public Seance() {
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
    @JoinColumn(name = "film_id")
    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id")
    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }
    @Basic
    @Column(name="debut",nullable = false)
    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    @OneToMany(mappedBy = "seance")
    public Set<Billet> getBillets() {
        return billets;
    }

    public void setBillets(Set<Billet> billets) {
        this.billets = billets;
    }

    public SeanceChamp toSeanceChamp()
    {   return new SeanceChamp(id,salle,film,debut);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seance)) return false;
        Seance seance = (Seance) o;
        return getId().equals(seance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
