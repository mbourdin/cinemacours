package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.MsgDao;
import mbourdin.cinema_cours.dao.TmdbFilmDao;
import mbourdin.cinema_cours.model.MsgToAdmin;
import mbourdin.cinema_cours.model.TmdbFilm;
import mbourdin.cinema_cours.service.FilmIdImport;
import mbourdin.cinema_cours.service.FilmStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.BufferedReader;

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
        return "/index";
    }

    @GetMapping("/msgliste/nonlus")
    public String listeMsgNonLus(Model m, @SessionAttribute Boolean admin)
    {
        m.addAttribute("msgs", msgDao.findAllByLuFalse());
        return ("/admin/msgliste");
    }
    @GetMapping("msgliste/all")
    public String listeMsg(Model m,@SessionAttribute Boolean admin)
    {
        m.addAttribute("msgs", msgDao.findAll());
        return ("/admin/msgliste");
    }
    @GetMapping("/msg/detail/{id}")
    public String msg(Model m,@SessionAttribute Boolean admin,@PathVariable Integer id)
    {
        MsgToAdmin msg= msgDao.findById(id).get();
        m.addAttribute("msg",msg);
        msg.setLu(Boolean.TRUE);
        msgDao.save(msg);
        return "/admin/msg";
    }
    @GetMapping("/msg/delete/{id}")
    public String msgDelete(Model m,@SessionAttribute Boolean admin,@PathVariable Integer id)
    {
        msgDao.deleteById(id);
        return "redirect:/admin/msgliste/nonlus";
    }
}
