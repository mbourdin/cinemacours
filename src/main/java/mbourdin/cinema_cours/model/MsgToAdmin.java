package mbourdin.cinema_cours.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="message")
public class MsgToAdmin {
    private Integer Id;
    private String texte;
    private LocalDate date;
    private String email;
    private Boolean lu;
    public MsgToAdmin()
    {   date=LocalDate.now();
        lu=false;
    }
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
    @Column(name="texte",nullable=false)
    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }
    @Basic
    @Column(name = "date",nullable=false)
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    //Bien que l'email puisse appartenir a un utilisateur enregistré, ce n'est pas
    // nécessairement le cas, l'éventuelle recherche se fera de maniere purement applicative
    // par le DAO utilisateur, la création d'un message  par un tilisateur connecte sera
    // automatiquement affectée a l'email de cet utilisateur.
    // il est donc normal de ne pas faire apparaitre ce lien au niveau de la base de données
    @Basic
    @Column(name="email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Basic
    @Column(name="lu")
    public Boolean isLu() {
        return lu;
    }

    public void setLu(Boolean lu) {
        this.lu = lu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MsgToAdmin)) return false;
        MsgToAdmin that = (MsgToAdmin) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTexte(), getDate(), getEmail(), lu);
    }
}
