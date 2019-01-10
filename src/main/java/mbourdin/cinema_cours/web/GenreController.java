package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.GenreDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.service.GenreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/genre")
public class GenreController {

    @Autowired
    FilmDao filmDao;

    GenreManager genreManager;

    @GetMapping("/liste")
    public String listeGenres(Model m)
    {   m.addAttribute("genres",genreManager.getAll());
        return "/genre/liste";
    }

    @GetMapping("/create")
    public String createGenre(Model m)
    {   m.addAttribute("genre",new Genre());
        return "/genre/create";
    }
    @GetMapping("/update/{id}")
    String updateGenre(Model m,@PathVariable Long id)
    {   Genre genre=genreManager.getById(id);
        m.addAttribute("genre",genre);
        return "/genre/create";
    }
    @PostMapping("/create")
    public String doCreateGenre(@ModelAttribute Genre genre, @SessionAttribute Boolean admin)
    {   if(admin==Boolean.TRUE)
        {   genreManager.save(genre);
        }
        return "redirect:/genre/liste";
    }
    @GetMapping("/delete/{id}")
    public String deletegenre(@SessionAttribute Boolean admin,@PathVariable Long id)
    {   if(admin==Boolean.TRUE)
        {   genreManager.delete(id);
        }
        return "redirect:/genre/liste";
    }
    @GetMapping("creerAssociation")
    public String associer(Model m)
    {   Set<Film> films=filmDao.findAll();
        List<Genre> genres=genreManager.getAll();
        m.addAttribute("films",films);
        m.addAttribute("genres",genres);
        return "/genre/associer";
    }
    @PostMapping("/associer")
    public String associerGenreFilm(@RequestParam Long genreId,@RequestParam Long filmId,@SessionAttribute Boolean admin)
    {   if(admin==Boolean.TRUE) {
        Genre genre = genreManager.getById(genreId);
        Film film = filmDao.findById(filmId).get();
        genre.addFilm(film);
        genreManager.save(genre);
        }
        return "redirect:/genre/liste";
    }
    @GetMapping("dissoudreAssociation")
    public String dissocier(Model m)
    {   Set<Film> films=filmDao.findAll();
        List<Genre> genres=genreManager.getAll();
        m.addAttribute("films",films);
        m.addAttribute("genres",genres);
        return "/genre/dissocier";
    }
    @PostMapping("/dissocier")
    public String dissocierGenreFilm(@RequestParam Long genreId,@RequestParam Long filmId,@SessionAttribute Boolean admin)
    {   if(admin==Boolean.TRUE) {
        Genre genre = genreManager.getById(genreId);
        Film film = filmDao.findById(filmId).get();
        genre.removeFilm(film);
        genreManager.save(genre);
        }
        return "redirect:/genre/liste";
    }
    /**
     *
     * @param genreManager
     */
    public GenreController(GenreManager genreManager){
        this.genreManager = genreManager;
        assert(genreManager != null);
    }

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("")
    public String main(Model model){
        model.addAttribute("genres", genreManager.getAll());
        model.addAttribute("newgenre", new Genre());
        return "genre/form";
    }
}
