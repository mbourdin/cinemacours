package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.GenreDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/genre")
public class GenreController {
    @Autowired
    GenreDao genreDao;
    @Autowired
    FilmDao filmDao;
    @GetMapping("/liste")
    public String listeGenres(Model m)
    {   m.addAttribute("genres",genreDao.findAll());
        return "/genre/liste";
    }

    @GetMapping("/create")
    public String createGenre(Model m)
    {   m.addAttribute("genre",new Genre());
        return "/genre/create";
    }
    @GetMapping("/update/{id}")
    String updateGenre(Model m,@PathVariable Long id)
    {   Genre genre=genreDao.findById(id).get();
        m.addAttribute("genre",genre);
        return "/genre/create";
    }
    @PostMapping("/create")
    public String doCreateGenre(@ModelAttribute Genre genre, @SessionAttribute Boolean admin)
    {   if(admin==Boolean.TRUE)
        {   genreDao.save(genre);
        }
        return "redirect:/genre/liste";
    }
    @GetMapping("/delete/{id}")
    public String deletegenre(@SessionAttribute Boolean admin,@PathVariable Long id)
    {   if(admin==Boolean.TRUE)
        {   genreDao.deleteById(id);
        }
        return "redirect:/genre/liste";
    }
    @GetMapping("creerAssociation")
    public String associer(Model m)
    {   Set<Film> films=filmDao.findAll();
        Set<Genre> genres=genreDao.findAll();
        m.addAttribute("films",films);
        m.addAttribute("genres",genres);
        return "/genre/associer";
    }
    @PostMapping("/associer")
    public String associerGenreFilm(@RequestParam Long genreId,@RequestParam Long filmId,@SessionAttribute Boolean admin)
    {   if(admin==Boolean.TRUE) {
        Genre genre = genreDao.findById(genreId).get();
        Film film = filmDao.findById(filmId).get();
        genre.addFilm(film);
        genreDao.save(genre);
        }
        return "redirect:/genre/liste";
    }
    @GetMapping("dissoudreAssociation")
    public String dissocier(Model m)
    {   Set<Film> films=filmDao.findAll();
        Set<Genre> genres=genreDao.findAll();
        m.addAttribute("films",films);
        m.addAttribute("genres",genres);
        return "/genre/dissocier";
    }
    @PostMapping("/dissocier")
    public String dissocierGenreFilm(@RequestParam Long genreId,@RequestParam Long filmId,@SessionAttribute Boolean admin)
    {   if(admin==Boolean.TRUE) {
        Genre genre = genreDao.findById(genreId).get();
        Film film = filmDao.findById(filmId).get();
        genre.removeFilm(film);
        genreDao.save(genre);
        }
        return "redirect:/genre/liste";
    }
}
