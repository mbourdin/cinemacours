package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.SalleDao;
import mbourdin.cinema_cours.dao.SeanceDao;
import mbourdin.cinema_cours.dao.TarifDao;
import mbourdin.cinema_cours.model.Seance;
import mbourdin.cinema_cours.auxiliaire.SeanceChamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    @Autowired
    TarifDao tarifDao;

    @GetMapping("/today")
    public String seancesdujour(Model m)
    {
        List<Seance> seances=
                seanceDao.getAllByDebutIsGreaterThanEqualAndDebutIsLessThanEqual(LocalDateTime.now().minusMinutes(15),LocalDateTime.now().plusDays(2));
        m.addAttribute("seances",seances);
        m.addAttribute("formatter",formatter);
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public String doCreerSeance(@ModelAttribute SeanceChamp seanceChamp, RedirectAttributes attributes)
    {       List<Seance> seances=seanceDao.getAllByDebutIsGreaterThanEqualAndDebutIsLessThanEqual(LocalDateTime.parse(seanceChamp.getDebut(),formatter).minusMinutes(240),LocalDateTime.parse(seanceChamp.getDebut(),formatter).plusMinutes(240));
            if (seances==null) seances=new ArrayList<>();
            Seance seance;
             Optional<Seance> seanceOptional=seanceDao.findById(seanceChamp.getSeanceId());
                if(seanceOptional.isPresent())
                {seance = seanceOptional.get();
                    seances.remove(seance);
                }
                else
                {seance=new Seance();}
        seance.setFilm(filmDao.findById(seanceChamp.getFilmId()).get());
        seance.setSalle(salleDao.findById(seanceChamp.getSalleId()).get());
        seance.setDebut(LocalDateTime.parse(seanceChamp.getDebut(),formatter));
        seance.setTarif(tarifDao.findById(seanceChamp.getTarifId()).get());
                for(Seance loopSeance : seances)
                {   if (loopSeance.collides(seance))
                    {   attributes.addFlashAttribute("flashMessage","Echec de creation/modification de séance : collision d'horaires/salle");
                        System.out.println("collision");
                        return "redirect:/seance/all";

                    }

                }


                //TODO assurer l'absence de collision entre seances, niveau applicatif ou niveau  BDD?
                seanceDao.save(seance);
        attributes.addFlashAttribute("flashMessage","ajout de séance réussi"+seance.toString());
        return "redirect:/seance/all";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/create")
    public String creerSeance(Model m)
    {   SeanceChamp seanceChamp=new SeanceChamp();
        m.addAttribute("seanceChamp",seanceChamp);
        m.addAttribute("salles",salleDao.findAllByActiveTrue());
        m.addAttribute("films",filmDao.findAll());
        m.addAttribute("tarifs",tarifDao.findAllByActifIsTrue());
        return "seance/create";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteSeance(@PathVariable long id)
    {   seanceDao.delete(seanceDao.findById(id).get());
        return "redirect:/seance/all";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/update/{id}")
    public String updateSeance(@PathVariable long id,Model m)
    {   Seance seance=seanceDao.findById(id).get();
        SeanceChamp seanceChamp=seance.toSeanceChamp();
        m.addAttribute("seanceChamp",seanceChamp);
        m.addAttribute("films",filmDao.findAll());
        m.addAttribute("tarifs",tarifDao.findAllByActifIsTrue());
        return "seance/create";
    }
}
