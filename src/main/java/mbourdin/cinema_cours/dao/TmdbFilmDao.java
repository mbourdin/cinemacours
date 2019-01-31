package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.TmdbFilm;
import org.springframework.data.repository.CrudRepository;

public interface TmdbFilmDao extends CrudRepository<TmdbFilm,Long> {
}
