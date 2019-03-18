package mbourdin.cinema_cours.dao;


import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface FilmDao extends PagingAndSortingRepository<Film, Long> {

    ArrayList<Film> findAllByOrderByTitreAsc();
    @Override
    @NonNull
    ArrayList<Film> findAll();
    Optional<Film> findByTmdbId(Long tmdbId);
    Page<Film> findAllByGenresContains(Genre genre, Pageable pageable);
    Page<Film> findAllByTitreContainingOrTitreOriginalContaining(String str,String str2, Pageable pageable);
}
