package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.PersonneDao;
import mbourdin.cinema_cours.dao.PlayDao;
import mbourdin.cinema_cours.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/play")
public class PlayController {
    @Autowired
    PlayDao playDao;
    @Autowired
    FilmDao filmDao;
    @Autowired
    PersonneDao personneDao;

    @GetMapping("/create")
    public String createPlay(Model m)
    {   Set<Film> films=filmDao.findAll();

        Set<Personne> acteurs=personneDao.findAll();
        Play play=new Play();
        m.addAttribute("play",play);
        m.addAttribute("films",films);
        m.addAttribute("acteurs",acteurs);

        return "/play/create";
    }

    @PostMapping("/create")
    public String doCreatePlay(@ModelAttribute Play play,@RequestParam Long film)
    {   play.setFilm(filmDao.findById(film).get());
        play.setPersonne(personneDao.findById(play.getPersonne().getId()).get());
        play=playDao.save(play);
        play.getPersonne().addPlay(play);
        play.getFilm().addPlay(play);
        return "redirect:/play/liste";
    }
    @PostMapping("/filmform")
    public String addPlayToForm(Model m,@ModelAttribute Play newrole,@RequestParam Long film_id)
    {   newrole.setFilm(filmDao.findById(film_id).get());
        newrole=playDao.save(newrole);
        Film film=filmDao.findById(film_id).get();
        newrole.getPersonne().addPlay(newrole);
        newrole.getFilm().addPlay(newrole);
        m.addAttribute("title","creation film");
        m.addAttribute("personnes",personneDao.findAll());
        m.addAttribute("readonly",Boolean.TRUE);
        m.addAttribute("newrole",new Play());
        m.addAttribute("film",film);
        return "/film/create";
    }
    @GetMapping("delfromfilm/{id}")
    public String deleteRoleFromFilm(Model m,@PathVariable Long id,@SessionAttribute Boolean admin)
    {   Play play=playDao.findById(id).get();

        Film film=filmDao.findById(play.getFilm().getId()).get();
        deleteRole(id);
        film.setRoles(playDao.findAllByFilm_IdOrderByNumeroAsc(film.getId()));

        m.addAttribute("title","creation film");
        m.addAttribute("personnes",personneDao.findAll());
        m.addAttribute("readonly",Boolean.TRUE);
        m.addAttribute("newrole",new Play());
        m.addAttribute("film",film);
        return "/film/create";
    }
    @GetMapping("/liste")
    public String listeRoles(Model m){
        m.addAttribute("plays",playDao.findAll());
        return "/play/liste";
    }
    @GetMapping("/update/{id}")
    public String update(Model m,@PathVariable Long id)
    {
        m.addAttribute("play",playDao.findById(id).get());
        m.addAttribute("readonly",Boolean.TRUE);
        return "/play/create";
    }
    @GetMapping("delete/{id}")
    public String deleteRole(@PathVariable Long id){

            Play play=playDao.findById(id).get();
            play.getFilm().deletePlay(play);
            play.getPersonne().deletePlay(play);
            playDao.deleteById(id);
        return "redirect:/play/liste";
    }
}
