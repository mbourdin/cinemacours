package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Commande;
import org.springframework.data.repository.CrudRepository;

public interface CommandeDao extends CrudRepository<Commande,Long> {
}
