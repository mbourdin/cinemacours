package mbourdin.cinema_cours.api;


import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.service.FilmManager;
import mbourdin.cinema_cours.service.GenreManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@Validated
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/genre")
public class GenreRestController {

    private GenreManager genreManager;
    private FilmManager filmManager;

    /**
     * Constructeur permettant l'injection de beans
     * @param genreManager le service de gestion du Genre du film
     */
    public GenreRestController(GenreManager genreManager,FilmManager filmManager){
        this.genreManager=genreManager;
        assert(genreManager != null);
        this.filmManager=filmManager;
        assert(filmManager != null);
    }

    /**
     *
     * @param id l'id du Genre recherché
     * @return le Genre trouvé
     */

    /*
    @GetMapping("{id}")
    public Genre get(@PathVariable("id") long id){
        return genreManager.getById(id);
    }
*/

    /**
     *
     * @return la liste des genres dans l'ordre defini par genremanager
     */

    /*@GetMapping("")
    public List<Genre> getAll() {
        return genreManager.getAll();
    }
*/
    /**
     *
     * @param genre le Genre a sauvegarder
     * @return identique au parametre genre
     */
    @PostMapping("/crud")
    public Genre add(@RequestBody Genre genre){
        if (genre.getName().isEmpty()) throw new IllegalArgumentException("Name is empty");
        return genreManager.save(genre);
    }

    /**
     *
     * @param genre le Genre a sauvegarder
     * @return identique au parametre genre
     */
    @PutMapping("/crud")
    public Genre mod(@RequestBody Genre genre){
        return genreManager.save(genre);
    }

    /**
     *
     * @param genre le Genre à deleteByUser
     * @return l'objet Genre qui a été supprimé de la base
     */
    @DeleteMapping("/crud")
    public Genre rm(@RequestBody Genre genre){
        return genreManager.delete(genre.getId());
    }

    /**
     *
     * @param id l'identifiant du Genre a deleteByUser
     * @return l'objet Genre qui a été supprimé de la base
     */
    @GetMapping("/rm/{id}")
    public Genre rm(@PathVariable("id")long id){
        return genreManager.delete(id);
    }
    @DeleteMapping("/film/{id}")
    public Genre dissociateFilm(@RequestBody Genre genre,@PathVariable Long id)
    {   genre=genreManager.getById(genre.getId());
        Film film=filmManager.getById(id);
        film.removeGenre(genre);
        filmManager.save(film);
        return genre;
    }
    @PostMapping("/film/{id}")
    public  Genre associateFilm(@RequestBody Genre genre,@PathVariable Long id)
    {   genre=genreManager.getById(genre.getId());
        Film film=filmManager.getById(id);
        film.addGenre(genre);
        filmManager.save(film);
        return genre;

    }
}
