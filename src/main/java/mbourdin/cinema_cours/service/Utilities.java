package mbourdin.cinema_cours.service;
import mbourdin.cinema_cours.model.Billet;
import mbourdin.cinema_cours.model.Commande;
import mbourdin.cinema_cours.model.Seance;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
public abstract class Utilities {
    public static boolean isPowerOfTwo(int i){
        return (i&(i-1))==0;
    }
    public static void setPermissions(HttpSession session,Utilisateur user)
    {   if (user==null) throw new RuntimeException("Utilisateur null dans Utilities.setPermissions");
        session.setAttribute("user",user);
        session.setAttribute("panier",new Panier());
        Commande commande=new Commande();
        session.setAttribute("commande",commande);
        if(user.hasType(Utilisateur.admin))
        {   session.setAttribute("admin",Boolean.TRUE);
        }
        else {
            session.setAttribute("admin", Boolean.FALSE);
        }
        if(user.hasType(Utilisateur.vendeur))
        {   session.setAttribute("vendeur",Boolean.TRUE);
        }
        else {
            session.setAttribute("vendeur", Boolean.FALSE);
        }
        if (user.hasType(Utilisateur.normal))
        {   session.setAttribute("normalUser",Boolean.TRUE);
        }
        else
        {   session.setAttribute("normalUser",Boolean.FALSE);
        }
        if (user.hasType(Utilisateur.moderateur))
        {   session.setAttribute("moderateur",Boolean.TRUE);

        }
        else
        {  session.setAttribute("moderateur",Boolean.FALSE);
        }
    }
    public static String ajouterACommande(Integer placesPrixNorm,Integer placesPrixReduit, Panier panier, HttpSession session, Seance seance)
    {   for (int i = 0; i < placesPrixNorm; i++) {
        Billet billet = new Billet();
        billet.setSeance(seance);
        panier.add(billet);
        billet.setCommande(null);
        billet.setPrix(seance.getTarif().getNormal());
        }
        for (int i = 0; i < placesPrixReduit; i++) {
            Billet billet = new Billet();
            billet.setSeance(seance);
            panier.add(billet);
            billet.setCommande(null);
            billet.setPrix(seance.getTarif().getReduit());
        }
        session.setAttribute("strings",panier.toStrings());
        if(session.getAttribute("vendeur")!=Boolean.TRUE) {
            return "redirect:/user/panier/detail";
        }
        else
        {
            return  "redirect:/vendeur/panier/detail";
        }
    }
}


