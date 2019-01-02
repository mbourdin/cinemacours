package mbourdin.cinema_cours.model;

import javax.persistence.*;
import java.time.LocalDate;

import java.util.Set;

@Entity
@Table(name="news")
public class NewsLetter {
    private Integer Id;
    private String texte;
    private LocalDate date;
    private Set<Utilisateur> utilisateurs;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    @Basic
    @Column(name="texte",nullable = false)
    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    @Basic
    @Column(name="date",nullable = false)
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "user_news",
            joinColumns = @JoinColumn(name = "news_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"))
    public Set<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }


    public void addUtilisater(Utilisateur utilisateur)
    {   if(!utilisateurs.contains(utilisateur))
    {
        utilisateurs.add(utilisateur);
        if (!utilisateur.getNewsLetters().contains(this)) {
            utilisateur.addNewsLetter(this);
        }
    }

    }
    public void removeUtilisateur(Utilisateur utilisateur)
    {   if(utilisateurs.contains(utilisateur))
        {
            utilisateurs.remove(utilisateur);
            if (utilisateur.getNewsLetters().contains(this)) {
                utilisateur.removeNewsletter(this);
            }
        }
    }
}
