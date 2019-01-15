package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.SalleDao;
import mbourdin.cinema_cours.dao.SeanceDao;
import mbourdin.cinema_cours.model.Seance;
import mbourdin.cinema_cours.auxiliaire.SeanceChamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seance")
public class SeanceController {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    @Autowired
    SeanceDao seanceDao;
    @Autowired
    FilmDao filmDao;
    @Autowired
    SalleDao salleDao;
    @GetMapping("/today")
    public String seancesdujour(Model m)
    {
        List<Seance> seances=
                seanceDao.getAllByDebutIsGreaterThanEqualAndDebutIsLessThanEqual(LocalDateTime.now(),LocalDateTime.now().plusDays(1));
        m.addAttribute("seances",seances);
        return "seance/liste";
    }
    @GetMapping("/semaine")
    public String seancesdelasemaine(Model m)
    {
        List<Seance> seances=seanceDao.getAllByDebutIsGreaterThanEqualAndDebutIsLessThanEqual(LocalDateTime.now(),LocalDateTime.now().plusDays(7));
        m.addAttribute("seances",seances);
        m.addAttribute("formatter",formatter);
        return "seance/liste";
    }
    @GetMapping("/all")
    public String toutesLesSeances(Model m)
    {   m.addAttribute("seances",seanceDao.findAll());
        m.addAttribute("formatter",formatter);
        return "seance/liste";
    }

    @PostMapping("create")
    public String doCreerSeance(@ModelAttribute SeanceChamp seanceChamp,@SessionAttribute Boolean admin)
    {

        if(admin!=null&&admin.equals(Boolean.TRUE))
        {
            Seance seance;


             Optional<Seance> seanceOptional=seanceDao.findById(seanceChamp.getSeanceId());
                if(seanceOptional.isPresent())
                {seance = seanceOptional.get();}
                else
                {seance=new Seance();}
                seance.setFilm(filmDao.findById(seanceChamp.getFilmId()).get());
                seance.setSalle(salleDao.findById(seanceChamp.getSalleId()).get());
                seance.setDebut(LocalDateTime.parse(seanceChamp.getDebut(),formatter));

                //TODO assurer l'absence de collision entre seances, niveau applicatif ou niveau  BDD?
                seanceDao.save(seance);
        }
        return "redirect:/seance/all";
    }

    @GetMapping("/create")
    public String creerSeance(Model m)
    {   SeanceChamp seanceChamp=new SeanceChamp();
        m.addAttribute("seanceChamp",seanceChamp);
        m.addAttribute("films",filmDao.findAll());
        return "/seance/create";
    }

    @GetMapping("/delete/{id}")
    public String deleteSeance(@PathVariable long id,@SessionAttribute Boolean admin)
    {
        if(admin!=null&&admin.equals(Boolean.TRUE))
        {   seanceDao.delete(seanceDao.findById(id).get());
        }
        return "redirect:/seance/all";
    }
    @GetMapping("/update/{id}")
    public String deleteSeance(@PathVariable long id,Model m)
    {   Seance seance=seanceDao.findById(id).get();
        SeanceChamp seanceChamp=seance.toSeanceChamp();
        m.addAttribute("seanceChamp",seanceChamp);
        m.addAttribute("films",filmDao.findAll());
        return "/seance/create";
    }
}
