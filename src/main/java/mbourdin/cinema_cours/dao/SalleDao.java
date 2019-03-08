package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Salle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SalleDao extends CrudRepository<Salle,Integer> {
    List<Salle> findAllByActiveTrue();
    List<Salle> findAll();
}
