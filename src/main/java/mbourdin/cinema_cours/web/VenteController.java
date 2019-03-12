package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.BilletDao;
import mbourdin.cinema_cours.dao.CommandeDao;
import mbourdin.cinema_cours.dao.SeanceDao;
import mbourdin.cinema_cours.model.Billet;
import mbourdin.cinema_cours.model.Commande;
import mbourdin.cinema_cours.model.Seance;
import mbourdin.cinema_cours.model.Utilisateur;
import mbourdin.cinema_cours.service.Panier;
import mbourdin.cinema_cours.service.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/vendeur")
@PreAuthorize("hasAuthority('VENDEUR')")
public class VenteController {
    @Autowired
    SeanceDao seanceDao;
    @Autowired
    CommandeDao commandeDao;
    @Autowired
    BilletDao billetDao;
    @GetMapping("/panier/detail")
    String detailPanier(Model m, @SessionAttribute Panier panier)
    {   m.addAttribute("strings",panier.toStrings());
        return "panier/detail";
    }
    @GetMapping("/commande/{id}")
    String commander(@PathVariable Long id, Model m) {
        m.addAttribute("seance", seanceDao.findById(id).get());
        m.addAttribute("action","/vendeur/commande");
        return "panier/create";
    }

    @PostMapping("/commande")
    String ajouterACommande(@RequestParam Integer placesNorm,@RequestParam Integer placesReduit, @SessionAttribute Panier panier, @RequestParam Long id, HttpSession session) {
        Seance seance=seanceDao.findById(id).get();
        //L'appel a une methode externe a la classe est justifiee par la necessite qu'a un utilisateur normal de pouvoir lui aussi commander des places pour une seances mais depuis un autre point d'accès
        return Utilities.ajouterACommande(placesNorm,placesReduit,panier,session,seance);
    }
    @GetMapping("/pay")
    String payer(Model m, HttpSession session, @SessionAttribute Panier panier, @SessionAttribute Utilisateur user, @SessionAttribute Commande commande)
    {   if(commande.getId()==null)
    {   commande=new Commande(panier,user);
        commande.setPaye(true);
        commandeDao.save(commande);
        for(Billet billet : commande.getBillets())
        {
            billetDao.save(billet);

             /*  billet.getSeance().getSalle().getId();
               billet.getSeance().getFilm().getTitre();
               billet.getSeance().getDebut();*/
        }
        session.setAttribute("panier",new Panier());
    }

        return "redirect:/seance/today";
    }
}
