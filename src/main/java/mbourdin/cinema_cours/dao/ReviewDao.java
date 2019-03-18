package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Review;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReviewDao extends PagingAndSortingRepository<Review,Long> {
       Review findByFilmAndUtilisateur(Film film, Utilisateur utilisateur);

       Set<Review> findAllByFilmAndEtat(Film film,int etat);
       Set<Review> findAllByEtat(int etat);
       Boolean existsByFilmAndUtilisateur(Film film,Utilisateur utilisateur);
       List<Review> findAllByFilmAndEtatOrderByDateDesc(Film film,int etat);
       Set<Review> findAllByUtilisateur(Utilisateur utilisateur);
       Page<Review> findAllByFilm(Film film, Pageable pageable);
       Page<Review> findAllByUtilisateur(Utilisateur utilisateur,Pageable pageable);

}
