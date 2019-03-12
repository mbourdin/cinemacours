package mbourdin.cinema_cours.model;

import javax.persistence.*;

@Entity
@Table(name="tarif")
public class Tarif {

    private Integer id;
    private double normal;
    private double reduit;
    private boolean actif;
    private String nom;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Basic
    @Column(name="normal")
    public double getNormal() {
        return normal;
    }

    public void setNormal(double normal) {
        this.normal = normal;
    }
    @Basic
    @Column(name="reduit")
    public double getReduit() {
        return reduit;
    }

    public void setReduit(double reduit) {
        this.reduit = reduit;
    }
    @Basic
    @Column(name="actif",nullable = false)

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
    @Basic
    @Column(name="nom")
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
