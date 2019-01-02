package mbourdin.cinema_cours.web;
// import mbourdin.cinema_cours.dao.DaoFilm;
import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.MsgDao;
import mbourdin.cinema_cours.dao.PersonneDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.MsgToAdmin;
import mbourdin.cinema_cours.model.Personne;
import mbourdin.cinema_cours.model.Utilisateur;
import mbourdin.cinema_cours.service.ImageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
public class MainController {
    @Autowired
    PersonneDao daoPersonne;

    @Autowired
    FilmDao daoFilm;

    @Autowired
    ImageManager imm;

    @Autowired
    MsgDao msgDao;
/*
    @PostMapping("/search")
    public String search(Model m, HttpServletRequest request)
    {   String str=request.getParameter("str");
        ArrayList<Film> films =daoFilm.findAll();
       // ArrayList<Play> roles=daoRole.searchByNom(str);
        ArrayList<Personne> personnes=daoPersonne.searchByNom(str);
        m.addAttribute("films",films);
       // m.addAttribute("roles",roles);
        m.addAttribute("personnes",personnes);
        return "search";
    }
*/


    @GetMapping("/")
    public String main(Model m){
        m.addAttribute("title","Accueil");
        return "index";
    }

    @GetMapping("/contact")
    public String contact(Model m)
    {           m.addAttribute("title","nous contacter");

        return "contact";
    }
    @GetMapping("/planDAcces")
    public String planDAcces(Model m)
    {        m.addAttribute("title","plan d'accès");
        return "planDAcces";
    }
    @PostMapping("sendMsgToAdmin")
    public String sendMsg(@RequestParam String texte,@RequestParam String email,Model m)
    {
        MsgToAdmin msg=new MsgToAdmin();
        msg.setDate(LocalDate.now());
        msg.setTexte(texte);
        msg.setEmail(email);
        msgDao.save(msg);
        m.addAttribute("message","votre message a l'admin a bien été envoyé");
        return "redirect:/";
    }
    @GetMapping("msgliste/nonlus")
    public String listeMsgNonLus(Model m,@SessionAttribute Boolean admin)
    {   if(admin!=null&&admin.equals(Boolean.TRUE)) {
        m.addAttribute("msgs", msgDao.findAllByLuFalse());
        }
        return ("/msgliste");
    }
    @GetMapping("msgliste/all")
    public String listeMsg(Model m,@SessionAttribute Boolean admin)
    {   if(admin!=null&&admin.equals(Boolean.TRUE)) {
        m.addAttribute("msgs", msgDao.findAll());
    }
        return ("/msgliste");
    }
    @GetMapping("/msg/detail/{id}")
    public String msg(Model m,@SessionAttribute Boolean admin,@PathVariable Integer id)
    {     if(admin!=null&&admin.equals(Boolean.TRUE)) {
            MsgToAdmin msg= msgDao.findById(id).get();
            m.addAttribute("msg",msg);
            msg.setLu(Boolean.TRUE);
        }
        return "/msg";
    }
    @GetMapping("/msg/delete/{id}")
    public String msgDelete(Model m,@SessionAttribute Boolean admin,@PathVariable Integer id)
    {     if(admin!=null&&admin.equals(Boolean.TRUE)) {
        msgDao.deleteById(id);
    }
        return "redirect:/msgliste/nonlus";
    }
}