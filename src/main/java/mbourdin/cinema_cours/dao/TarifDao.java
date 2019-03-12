package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Tarif;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TarifDao extends CrudRepository<Tarif,Integer> {
    List<Tarif> findAllByActifIsTrue();
    List<Tarif> findAll();
}
