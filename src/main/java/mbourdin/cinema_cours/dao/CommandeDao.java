package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Billet;
import mbourdin.cinema_cours.model.Commande;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CommandeDao extends CrudRepository<Commande,Long> {

}
