package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Salle;
import org.springframework.data.repository.CrudRepository;

public interface SalleDao extends CrudRepository<Salle,Integer> {
}
