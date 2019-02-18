package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.BilletDao;
import mbourdin.cinema_cours.dao.CommandeDao;
import mbourdin.cinema_cours.dao.SeanceDao;
import mbourdin.cinema_cours.model.*;
import mbourdin.cinema_cours.service.Panier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/panier")
public class BilleterieController {
    @Autowired
    BilletDao billetDao;
    @Autowired
    SeanceDao seanceDao;
    @Autowired
    CommandeDao commandeDao;


    @GetMapping("/payer")
    String payer(Model m,HttpSession session,@SessionAttribute Panier panier, @SessionAttribute Utilisateur user,@SessionAttribute Commande commande)
    {   if(commande.getId()==null)
        {   commande=new Commande(panier,user);
            commandeDao.save(commande);
            for(Billet billet : commande.getBillets())
            {
                billetDao.save(billet);

             /*  billet.getSeance().getSalle().getId();
               billet.getSeance().getFilm().getTitre();
               billet.getSeance().getDebut();*/
            }
            session.setAttribute("commande",commande);
            session.setAttribute("panier",new Panier());
        }

        return "panier/commande";
    }
    @GetMapping("/mesCommandes")
    String listerCommandes(@SessionAttribute Utilisateur user,Model m)
    {    Set<Commande> commandes= commandeDao.findAllByUtilisateur(user);
        m.addAttribute("commandes",commandes);
        return "/panier/listeCommandes";
    }
    @GetMapping("/commande/{id}")
    String commander(@PathVariable Long id, Model m) {
        m.addAttribute("seance", seanceDao.findById(id).get());
        return "/panier/create";
    }
    @GetMapping("/enCours")
    String getCmd(@SessionAttribute Utilisateur user,HttpSession session)
    {   Commande commande=commandeDao.findByPayeFalseAndUtilisateur(user);
        session.setAttribute("commande",commande);
        for(Billet billet : commande.getBillets())
        {
            billetDao.save(billet);

            /*billet.getSeance().getSalle().getId();
            billet.getSeance().getFilm().getTitre();
            billet.getSeance().getDebut();*/
        }
        return "panier/commande";
    }


    @PostMapping("/commande")
    String ajouterACommande(@RequestParam Integer places, @SessionAttribute Panier panier, @RequestParam Long id, HttpSession session) {
        // Les appels suivants sont nécessaires pour initialiser completement l'objet panier
        Seance seance=seanceDao.findById(id).get();
        //seance.getDebut();
        //Film film=seance.getFilm();
        //film.getTitre();

        for (int i = 0; i < places; i++) {
            Billet billet = new Billet();
            billet.setSeance(seance);
            panier.add(billet);
            billet.setCommande(null);
            billet.setPrix(Seance.prixdefaut);
        }
        session.setAttribute("strings",panier.toStrings());

        return "redirect:/panier/detail";
    }
    @GetMapping("/detail")
    String detailPanier(Model m,@SessionAttribute Panier panier)
    {   m.addAttribute("strings",panier.toStrings());
        return "/panier/detail";
    }

}
