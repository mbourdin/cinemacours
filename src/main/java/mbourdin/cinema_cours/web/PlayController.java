package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.PersonneDao;
import mbourdin.cinema_cours.dao.PlayDao;
import mbourdin.cinema_cours.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
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
    public String doCreatePlay(@ModelAttribute Play play)
    {   play.setFilm(filmDao.findById(play.getFilm().getId()).get());
        play.setPersonne(personneDao.findById(play.getPersonne().getId()).get());
        playDao.save(play);
        return "redirect:/play/liste";
    }
    @PostMapping("/filmform")
    public String addPlayToForm(Model m,@ModelAttribute Film film,@ModelAttribute Play newrole,@RequestParam Long id)
    {   newrole.setFilm(filmDao.findById(id).get());
        playDao.save(newrole);
        film=filmDao.findById(id).get();
        film.setRoles(playDao.findAllByFilm_IdOrderByNumeroAsc(film.getId()));
        m.addAttribute("title","creation film");
        m.addAttribute("personnes",personneDao.findAll());
        m.addAttribute("readonly",Boolean.TRUE);
        m.addAttribute("newrole",new Play());
        m.addAttribute("film",film);
        return "/film/create";
    }
    @GetMapping("delfromfilm/{id}")
    public String deleteRoleFromFilm(Model m,@PathVariable Long id,@SessionAttribute Boolean admin)
    {   Film film=filmDao.findById(playDao.findById(id).get().getFilm().getId()).get();
        film.setRoles(playDao.findAllByFilm_IdOrderByNumeroAsc(film.getId()));
        this.deleteRole(id,admin);
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
    public String deleteRole(@PathVariable Long id,@SessionAttribute Boolean admin){

        if (admin==Boolean.TRUE) {
            playDao.deleteById(id);
        }
        return "redirect:/play/liste";
    }
}
