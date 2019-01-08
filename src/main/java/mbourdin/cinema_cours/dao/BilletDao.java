package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Billet;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface BilletDao extends CrudRepository<Billet,Long> {
    Set<Billet> findAllByCommande_PayeAndSeanceId(Boolean paye,Long id);
}
