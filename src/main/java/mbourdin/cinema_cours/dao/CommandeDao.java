package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Billet;
import mbourdin.cinema_cours.model.Commande;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface CommandeDao extends CrudRepository<Commande,Long> {
    Commande findCommandeByPayeFalseAndUtilisateur(Utilisateur utilisateur);
}
