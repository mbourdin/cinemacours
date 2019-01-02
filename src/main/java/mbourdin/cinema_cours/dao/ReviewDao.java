package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Review;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReviewDao extends CrudRepository<Review,Long> {
       Review findByFilmAndAndUtilisateur(Film film, Utilisateur utilisateur);
}
