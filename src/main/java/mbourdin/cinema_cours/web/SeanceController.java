package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.SalleDao;
import mbourdin.cinema_cours.dao.SeanceDao;
import mbourdin.cinema_cours.model.Seance;
import mbourdin.cinema_cours.service.SeanceChamp;
import mbourdin.cinema_cours.service.SeanceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seance")
public class SeanceController {
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
        return "seance/liste";
    }
    @GetMapping("/all")
    public String toutesLesSeances(Model m)
    {   m.addAttribute("seances",seanceDao.findAll());
        return "seance/liste";
    }
    @GetMapping("/combien")
    public String combien()
    {   return "/seance/combien";

    }

    @PostMapping("create")
    public String doCreerSeance(@ModelAttribute SeanceList seanceList,@SessionAttribute Boolean admin)
    {

        if(admin!=null&&admin.equals(Boolean.TRUE))
        {
            Seance seance;
            for (SeanceChamp champs : seanceList.getChamps())
            {   Optional<Seance> seanceOptional=seanceDao.findById(champs.getSeanceId());
                if(seanceOptional.isPresent())
                {seance = seanceOptional.get();}
                else
                {seance=new Seance();}
                seance.setFilm(filmDao.findById(champs.getFilmId()).get());
                seance.setSalle(salleDao.findById(champs.getSalleId()).get());
                seance.setDebut(LocalDateTime.parse(champs.getDebut()));

                //TODO assurer l'absence de collision entre seances, niveau applicatif ou niveau  BDD?
                seanceDao.save(seance);
            }
        }
        return "redirect:/seance/all";
    }

    @GetMapping("/create")
    public String creerSeance(@RequestParam int n,Model m)
    {   return creerSeance(m,n);

    }
    @GetMapping("/create/{ns}")
    public String creerSeance(Model m,@PathVariable int ns)
    {

        SeanceList seanceList=new SeanceList();
        for(int i=0;i<ns;i++)
        {   Seance seance=new Seance();
            seanceList.add(seance.toSeanceChamp());
        }
        m.addAttribute("seanceList",seanceList);
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
    {   SeanceList seanceList=new SeanceList();
        Seance seance=seanceDao.findById(id).get();
        seanceList.add(seance.toSeanceChamp());
        m.addAttribute("seanceList",seanceList);
        m.addAttribute("films",filmDao.findAll());
        return "/seance/create";
    }
}
