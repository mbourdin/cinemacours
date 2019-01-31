package mbourdin.cinema_cours.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="play")
public class Play  implements Comparable<Play> {
    private Long id;
    private String nom;
    private int numero;
    private Film film;
    private Personne personne;



    public Play(){;}
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
    @JoinColumn(name = "person_id")
    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne acteur) {
        this.personne = acteur;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Basic
    @Column(name = "rank", nullable = true)
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


    public void addPlay()
    {   film.addPlay(this);
        personne.addPlay(this);

    }
    public void deletePlay()
    {   film.deletePlay(this);
        personne.deletePlay(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (!(obj instanceof Play))  return false;
        Play that=(Play) obj;
        return that.id.equals(this);
    }

    @Override
    public String toString() {
        return "Play{" +
                "nom='" + nom + '\'' +
                ", acteur=" + personne.getName() +

                ", numero=" + numero +
                ", film=" + film.getTitre() +
                '}';
    }
    @Override
    public int compareTo(Play o) {
        if (this.personne.getId()<o.personne.getId()) return -1;
        else if (personne.getId()>o.personne.getId()) return 1;
        else if (film.getId()<o.film.getId()) return -1;
        else if (film.getId()>o.film.getId()) return 1;
        else return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    public void setAll(Film film,Personne personne,Integer numero,String nom)
    {
        this.film=film;
        this.personne=personne;
        this.numero=numero;
        this.nom=nom;
    }
}
