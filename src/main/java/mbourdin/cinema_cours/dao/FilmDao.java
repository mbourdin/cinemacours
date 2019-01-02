package mbourdin.cinema_cours.dao;


import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;


public interface FilmDao extends CrudRepository<Film, Long> {
    @Override
    Set<Film> findAll();

    Set<Film> findAllByGenresContains(Genre genre);
}
