package mbourdin.cinema_cours.dao;


import mbourdin.cinema_cours.model.Play;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayDao extends CrudRepository<Play, Long> {
          List<Play> findAllByFilm_IdOrderByNumeroAsc(Long filmId);
          @Override
          List<Play> findAll();
     }
