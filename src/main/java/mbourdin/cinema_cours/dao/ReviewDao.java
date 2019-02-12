package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Review;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReviewDao extends CrudRepository<Review,Long> {
       Review findByFilmAndUtilisateur(Film film, Utilisateur utilisateur);
       Set<Review> findAllByFilmAndValideIsTrue(Film film);
       Set<Review> findAllByValideIsFalse();
       Boolean existsByFilmAndUtilisateur(Film film,Utilisateur utilisateur);
       List<Review> findAllByFilmAndValideIsTrueOrderByDateDesc(Film film);
}
