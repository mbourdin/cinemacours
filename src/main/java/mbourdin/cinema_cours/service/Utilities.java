package mbourdin.cinema_cours.service;
import mbourdin.cinema_cours.model.Commande;
import mbourdin.cinema_cours.model.Utilisateur;

import javax.servlet.http.HttpSession;
public abstract class Utilities {
    public static void setPermissions(HttpSession session,Utilisateur user)
    {   if (user==null) throw new RuntimeException("Utilisateur null dans Utilities.setPermissions");
        session.setAttribute("user",user);
        session.setAttribute("panier",new Panier());
        Commande commande=new Commande();
        session.setAttribute("commande",commande);
        if (user.getType()==Utilisateur.admin)
        {   session.setAttribute("admin",Boolean.TRUE);
            session.setAttribute("vendeur",Boolean.TRUE);
            session.setAttribute("normalUser",Boolean.TRUE);
        }else
        if (user.getType()==Utilisateur.vendeur)
        {   session.setAttribute("admin",Boolean.FALSE);
            session.setAttribute("vendeur",Boolean.TRUE);
            session.setAttribute("normalUser",Boolean.FALSE);
        }
        else
        {   session.setAttribute("admin",Boolean.FALSE);
            session.setAttribute("vendeur",Boolean.FALSE);
            session.setAttribute("normalUser",Boolean.TRUE);

        }
    }
}


