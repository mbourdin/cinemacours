package mbourdin.cinema_cours.web;
// import mbourdin.cinema_cours.dao.DaoFilm;
import mbourdin.cinema_cours.auxiliaire.Planning;
import mbourdin.cinema_cours.dao.*;
import mbourdin.cinema_cours.model.*;
import mbourdin.cinema_cours.service.FilmIdImport;
import mbourdin.cinema_cours.service.FilmStream;
import mbourdin.cinema_cours.service.ImageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Value("${flagPath}")
    String flagPath;

    @Autowired
    PersonneDao daoPersonne;

    @Autowired
    FilmDao daoFilm;

    @Autowired
    ImageManager imm;

    @Autowired
    MsgDao msgDao;

    @Autowired
    TmdbFilmDao tmdbFilmDao;
    @Autowired
    SalleDao salleDao;
    @Autowired
    SeanceDao seanceDao;
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
    public String main(Model m, HttpSession session){
        m.addAttribute("title","Accueil");
        Map<Integer,Planning> plannings =new HashMap<>();
        List<Salle> salles=salleDao.findAll();
        for(Salle salle:salles)
        {   plannings.put(salle.getId(),new Planning(salle, LocalDateTime.now(),seanceDao));
        }
        boolean empty=tmdbFilmDao.countAllBy()==0;
        m.addAttribute("empty",empty);
        m.addAttribute("salles",salles);
        m.addAttribute("plannings",plannings);
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
    public String sendMsg(@RequestParam String texte,@RequestParam String email)
    {
        MsgToAdmin msg=new MsgToAdmin();
        msg.setDate(LocalDate.now());
        msg.setTexte(texte);
        msg.setEmail(email);
        msgDao.save(msg);
        //m.addAttribute("message","votre message a l'admin a bien été envoyé");
        return "redirect:/";
    }


    @GetMapping("/error/{id}")
    public String error(@PathVariable Long id)
    {   return "error/"+id;
    }

    @RequestMapping(value = "/drapeau/{id}", method = RequestMethod.GET)
    public @ResponseBody
    void getImage0(HttpServletResponse response, @PathVariable("id") Long id) throws IOException, URISyntaxException {



        BufferedImage image = ImageIO.read(new File(flagPath+"/" + id+".jpg"));

        response.setContentType("image/jpg");
        OutputStream out;

        out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);

    }

    //Ceci est un mapping de test pour le TP de M AZRIA
    @GetMapping("/testimages")
    public String testimages() {
        return "testimages";
    }
}