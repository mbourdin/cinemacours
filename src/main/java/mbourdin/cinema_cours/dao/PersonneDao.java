package mbourdin.cinema_cours.dao;


import mbourdin.cinema_cours.model.Personne;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;


public interface PersonneDao extends CrudRepository<Personne, Long> {
    @Override
    Set<Personne> findAll();
}