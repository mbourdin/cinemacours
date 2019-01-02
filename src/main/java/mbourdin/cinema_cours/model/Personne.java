package mbourdin.cinema_cours.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="persons")
public class Personne {
    private long id;
    private String nom;
    private String prenom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate naissance;
    private String photopath;
    private List<Film> realise ;
    private List<Play> roles;

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
    @Column(name = "surname", nullable = false, length = 60)
    public String getNom() {
        return nom;
    }

    public void setNom(String surname) {
        this.nom = surname;
    }

    @Basic
    @Column(name = "givenname", nullable = true, length = 40)
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String givenname) {
        this.prenom = givenname;
    }

    @Basic
    @Column(name = "birthday", nullable = true)
    public LocalDate getNaissance() {
        return naissance;}

    public void setNaissance(LocalDate birthday) {
        this.naissance = birthday;
    }

    @Basic
    @Column(name = "image_path", nullable = true, length = 80)
    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String imagePath) {
        this.photopath = imagePath;
    }

    @OneToMany(mappedBy="realisateur")
    public List<Film> getRealise() {
        return realise;
    }

    public void setRealise(List<Film> realise) {
        this.realise = realise;
    }

    @OneToMany(mappedBy= "personne")
    public List<Play> getRoles() {
        return roles;
    }

    public void setRoles(List<Play> joue) {
        this.roles = joue;
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


    public void addFilm(Film film){
        if (!realise.contains(film))
        {   realise.add(film);
            film.setRealisateur(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Personne persons = (Personne) o;

        if (id != persons.id) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        return result;
    }

}
