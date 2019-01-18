package mbourdin.cinema_cours.api;

import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.service.FilmManager;
import mbourdin.cinema_cours.service.PersonneManager;
import mbourdin.cinema_cours.service.PlayManager;
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

    @PutMapping("/save/{datestring}")
    public Long saveFilm(@RequestBody Film film,@PathVariable String datestring)
    {
        LocalDate date=LocalDate.parse(datestring, DateTimeFormatter.ISO_DATE);
        film.setAnnee(date);
        return filmManager.save(film);
    }

}
