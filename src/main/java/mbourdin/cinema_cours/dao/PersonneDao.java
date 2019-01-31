package mbourdin.cinema_cours.dao;


import mbourdin.cinema_cours.model.Personne;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;


public interface PersonneDao extends CrudRepository<Personne, Long> {
    @Override
    Set<Personne> findAll();
    List<Personne> findAllByOrderByName();
    Personne findByTmdbId(Long id);
}
