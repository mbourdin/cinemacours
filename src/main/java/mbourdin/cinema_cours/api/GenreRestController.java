package mbourdin.cinema_cours.api;


import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.service.GenreManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@Validated
@RestController
@RequestMapping("/api/genre")
public class GenreRestController {

    private GenreManager genreManager;

    /**
     * Constructeur permettant l'injection de beans
     * @param genreManager le service de gestion du Genre du film
     */
    public GenreRestController(GenreManager genreManager){
        this.genreManager=genreManager;
        assert(genreManager != null);
    }

    /**
     *
     * @param id l'id du Genre recherché
     * @return le Genre trouvé
     */
    @GetMapping("{id}")
    public Genre get(@PathVariable("id") long id){
        return genreManager.getById(id);
    }

    /**
     *
     * @return la liste des genres dans l'ordre defini par genremanager
     */
    @GetMapping("")
    public List<Genre> getAll() {
        return genreManager.getAll();
    }

    /**
     *
     * @param genre le Genre a sauvegarder
     * @return identique au parametre genre
     */
    @PostMapping("")
    public Genre add(@RequestBody Genre genre){
        if (genre.getName().isEmpty()) throw new IllegalArgumentException("Name is empty");
        return genreManager.save(genre);
    }

    /**
     *
     * @param genre le Genre a sauvegarder
     * @return identique au parametre genre
     */
    @PutMapping("")
    public Genre mod(@RequestBody Genre genre){
        return genreManager.save(genre);
    }

    /**
     *
     * @param genre le Genre à supprimer
     * @return l'objet Genre qui a été supprimé de la base
     */
    @DeleteMapping("")
    public Genre rm(@RequestBody Genre genre){
        return genreManager.delete(genre.getId());
    }

    /**
     *
     * @param id l'identifiant du Genre a supprimer
     * @return l'objet Genre qui a été supprimé de la base
     */
    @GetMapping("/rm/{id}")
    public Genre rm(@PathVariable("id")long id){
        return genreManager.delete(id);
    }
}
