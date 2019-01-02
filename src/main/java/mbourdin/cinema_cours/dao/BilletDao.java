package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Billet;
import org.springframework.data.repository.CrudRepository;

public interface BilletDao extends CrudRepository<Billet,Long> {
}
