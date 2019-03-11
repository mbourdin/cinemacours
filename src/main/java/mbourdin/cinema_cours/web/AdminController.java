package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.MsgDao;
import mbourdin.cinema_cours.dao.SalleDao;
import mbourdin.cinema_cours.dao.SeanceDao;
import mbourdin.cinema_cours.dao.TmdbFilmDao;
import mbourdin.cinema_cours.model.MsgToAdmin;
import mbourdin.cinema_cours.model.Salle;
import mbourdin.cinema_cours.model.TmdbFilm;
import mbourdin.cinema_cours.service.FilmIdImport;
import mbourdin.cinema_cours.service.FilmStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    FilmIdImport filmIdImport;
    @Autowired
    TmdbFilmDao tmdbFilmDao;
    @Autowired
    MsgDao msgDao;
    @Autowired
    SalleDao salleDao;

    @GetMapping("/importFilms")
    public String testfilmstream()
    {

        FilmStream fs=new FilmStream();

        BufferedReader br=fs.getBr();
        TmdbFilm film;
        try{    String ligne=br.readLine();
            while(ligne!=null)
            {   film=filmIdImport.importedFilm(ligne);
                if(film!=null) {
                    tmdbFilmDao.save(film);

                }
                ligne=br.readLine();

            }
            System.out.println(filmIdImport.importedFilm(ligne));
        }catch (Exception e){e.printStackTrace();}
        return "index";
    }

    @GetMapping("/msgliste/nonlus")
    public String listeMsgNonLus(Model m, @SessionAttribute Boolean admin)
    {
        m.addAttribute("msgs", msgDao.findAllByLuFalse());
        return ("admin/msgliste");
    }
    @GetMapping("msgliste/all")
    public String listeMsg(Model m,@SessionAttribute Boolean admin)
    {
        m.addAttribute("msgs", msgDao.findAll());
        return ("admin/msgliste");
    }
    @GetMapping("/msg/detail/{id}")
    public String msg(Model m,@PathVariable Integer id)
    {
        MsgToAdmin msg= msgDao.findById(id).get();
        m.addAttribute("msg",msg);
        msg.setLu(Boolean.TRUE);
        msgDao.save(msg);
        return "admin/msg";
    }
    @GetMapping("/msg/delete/{id}")
    public String msgDelete(@PathVariable Integer id)
    {
        msgDao.deleteById(id);
        return "redirect:/admin/msgliste/nonlus";
    }
    @GetMapping("/salle/add")
    public String addSalle(Model m)
    {
        Salle salle=new Salle();
        m.addAttribute("salle",salle);
        return  "salle/form";
    }
    @GetMapping("/salle/mod/{id}")
    public String modSalle(Model m,@PathVariable Integer id)
    {
        Salle salle=salleDao.findById(id).get();
        m.addAttribute("salle",salle);
        return  "salle/form";
    }
    @PostMapping("/salle/create")
    public String createSalle(@ModelAttribute Salle salle)
    {   salleDao.save(salle);
        return "redirect:/admin/salle/liste";
    }
    @GetMapping("/salle/liste")
    public String listeSalles(Model m)
    {   List<Salle> salles = salleDao.findAll();
        m.addAttribute("salles",salles);
        return "salle/liste";
    }
}
