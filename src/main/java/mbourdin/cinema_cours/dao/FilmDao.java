package mbourdin.cinema_cours.dao;


import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface FilmDao extends PagingAndSortingRepository<Film, Long> {

    ArrayList<Film> findAllByGenresContains(Genre genre);
    ArrayList<Film> findAllByOrderByTitreAsc();
    @Override
    ArrayList<Film> findAll();
    Optional<Film> findByTmdbId(Long tmdbId);
}
