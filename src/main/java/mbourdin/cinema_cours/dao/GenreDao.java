package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface GenreDao extends CrudRepository<Genre,Long> {
    @Override
    Set<Genre> findAll();
    List<Genre> findAllByOrderByNameAsc();
    Genre findByName(String name);
    Genre findByTmdbId(Long id);
}
