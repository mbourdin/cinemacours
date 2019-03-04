package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.BilletDao;
import mbourdin.cinema_cours.dao.CommandeDao;
import mbourdin.cinema_cours.dao.SeanceDao;
import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.Billet;
import mbourdin.cinema_cours.model.Commande;
import mbourdin.cinema_cours.model.Seance;
import mbourdin.cinema_cours.model.Utilisateur;
import mbourdin.cinema_cours.service.CinemaUserService;
import mbourdin.cinema_cours.service.Panier;
import mbourdin.cinema_cours.service.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Set;


/* L'utilisateur connecté peut acceder
au détail et la mise a jour concernant
 UNIQUEMENT son propre compte*/
/* Toutes les autres fonctions de ce controlleur sont réservées a l'admin*/

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    CinemaUserService cinemaUserService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    BilletDao billetDao;
    @Autowired
    SeanceDao seanceDao;
    @Autowired
    CommandeDao commandeDao;

    @Autowired
    UserDao userDao;
    @Value("${pwprefix}")
    String prefix;
    @Value("${pwsuffix}")
    String suffix;
    @Value("${salt}")
    String salt;
    @GetMapping("/detail/{id}")
    public String detail(Model m, @PathVariable("id") Long id){
        Utilisateur user= userDao.findById(id).get();
        m.addAttribute("user",user);
        m.addAttribute("title", "détail de l'utilisateur "+user.getId());
        return "user/detail";
    }

    @GetMapping("/liste")
    public String listeUsers(Model m){
        m.addAttribute("title","liste des utilisateurs");
        m.addAttribute("listeUsers",userDao.findAll());
        return "user/liste";
    }

    @GetMapping("/update/{id}")
    public String updateUser(Model m,@PathVariable("id") Long id){
        m.addAttribute("title","MAJ User");
        m.addAttribute("newuser", userDao.findById(id).get());
        return "/user/create";
    }
    @GetMapping("selfupdate")
    public String selfUpdateUser(Model m,@SessionAttribute("user") Utilisateur user){
        m.addAttribute("title","MAJ User");
        m.addAttribute("newuser", user);
        return "/user/create";
    }
    @GetMapping("/create")
    public String createUser(Model m)
    {   m.addAttribute("title","creation User");
        Utilisateur newuser=new Utilisateur();
        System.out.println(newuser);
        m.addAttribute("newuser",newuser);
        return "/user/create";
    }

    @PostMapping("/create")
    public String doCreateUser(@ModelAttribute Utilisateur newuser,@RequestParam("password2") String password,@SessionAttribute("user") Utilisateur user)
    {   boolean modifypassword=false;
        if(!password.equalsIgnoreCase("")) {
            newuser.setPassword(password);
            modifypassword = true;
        }
        if((user.equals(newuser)&&user.getType()==newuser.getType())
            || (user.getType()==Utilisateur.admin)
        )
        {   //empecher l'admin de se supprimer ses propres droits d'administration
            if (user.getType()==Utilisateur.admin&&user.equals(newuser))
            {
                newuser.setType(Utilisateur.admin);
                newuser.setActif(true);
            }


            if(modifypassword)
            {
            cinemaUserService.saveWithNewPass(newuser);
            }
            else
            {
                cinemaUserService.saveWithSamePass(newuser);
            }
        }
        return "redirect:/user/liste";
    }
    @GetMapping("delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id,@SessionAttribute("user") Utilisateur user){
        Utilisateur utilisateur=userDao.findById(id).get();
        System.out.println(user);
        System.out.println(utilisateur);
        //empecher l'admin de se deleteByUser lui-même!
        if(!user.equals(utilisateur)){
            userDao.delete(utilisateur);
        }


        return "redirect:/user/liste";
    }

    @GetMapping("/panier/payer")
    String payer(Model m, HttpSession session, @SessionAttribute Panier panier, @SessionAttribute Utilisateur user, @SessionAttribute Commande commande)
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
    @GetMapping("/panier/enCours")
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
    @GetMapping("/panier/detail")
    String detailPanier(Model m,@SessionAttribute Panier panier)
    {   m.addAttribute("strings",panier.toStrings());
        return "/panier/detail";
    }
}
