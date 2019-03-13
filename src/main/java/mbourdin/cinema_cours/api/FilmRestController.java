package mbourdin.cinema_cours.api;

import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.model.Personne;
import mbourdin.cinema_cours.service.FilmManager;
import mbourdin.cinema_cours.service.PersonneManager;
import mbourdin.cinema_cours.service.PlayManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController

@RequestMapping("/api/film")
public class FilmRestController {
    private FilmManager filmManager;
    private PersonneManager personneManager;

    public FilmRestController(PlayManager playManager, FilmManager filmManager, PersonneManager personneManager) {

        assert (filmManager != null);
        this.filmManager = filmManager;
        assert (personneManager != null);
        this.personneManager = personneManager;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/save/{datestring}/{idreal}")
    public Long saveFilm(@RequestBody Film film,@PathVariable String datestring,@PathVariable Long idreal)
    {
        LocalDate date=LocalDate.parse(datestring, DateTimeFormatter.ISO_DATE);
        film.setAnnee(date);
        Personne realisateur=personneManager.getById(idreal);
        film.setRealisateur(realisateur);
        return filmManager.merge(film);
    }
    @RequestMapping(value = "/bypage/{pageNumber}", method = RequestMethod.GET)
    public Page<Film> queryByPage(@PathVariable Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber , 2);
        Page<Film> pageFilms = filmManager.listByPage(pageable);
        return pageFilms;
    }

}
