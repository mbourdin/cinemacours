package mbourdin.cinema_cours.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import mbourdin.cinema_cours.auxiliaire.SeanceChamp;
import mbourdin.cinema_cours.dao.TarifDao;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="seance")
public class Seance {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final double prixdefaut=9.00;
    private Long id;
    private Film film;
    private Salle salle;
    private LocalDateTime debut;
    private Set<Billet> billets;
    private Tarif tarif;



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
    @JsonIgnore
    public Set<Billet> getBillets() {
        return billets;
    }

    public void setBillets(Set<Billet> billets) {
        this.billets = billets;
    }

    public SeanceChamp toSeanceChamp()
    {   return new SeanceChamp(id,salle,film,debut,tarif);
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

    public String formattedDate()
    {   return debut.format(formatter);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarif_id")
    public Tarif getTarif() {
        return tarif;
    }

    public void setTarif(Tarif tarif) {
        this.tarif = tarif;
    }

    public boolean collides(Seance seance)
    {   System.out.println(this);
        System.out.println(seance);

        if(this.getFilm().getDuree()==null) this.getFilm().setDuree(240);
        if(seance.getFilm().getDuree()==null) seance.getFilm().setDuree(240);
        if(seance.getDebut().plusMinutes(seance.getFilm().getDuree()).isBefore(this.getDebut())) return false;
        if(this.getDebut().plusMinutes(this.getFilm().getDuree()).isBefore(seance.getDebut())) return false;
        return (this.salle.equals(seance.salle));
    }
    @Override
    public String toString()
    {   StringBuilder sb=new StringBuilder();
        sb.append("id=")
                .append(id)
                .append("film=")
                .append(film.getId())
                .append(":")
                .append(film.getTitre())
                .append(":salle=")
                .append(salle.getNom())
                .append("date:")
                .append(formattedDate())
                .append("tarif=")
                .append(tarif.getNom());
        return sb.toString();
    }
}
