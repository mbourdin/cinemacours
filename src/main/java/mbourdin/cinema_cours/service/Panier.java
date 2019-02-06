package mbourdin.cinema_cours.service;

import mbourdin.cinema_cours.model.Billet;

import java.util.*;

public class Panier {
    List<Billet> billets;

    public Panier(List<Billet> billets) {
        this.billets = billets;
    }

    public Panier() {this.billets=new ArrayList<Billet>();
    }

    public List<Billet> getBillets() {
        return billets;
    }

    public void setBillets(List<Billet> billets) {
        this.billets = billets;
    }
    public void add(Billet billet)
    {   billets.add(billet);

    }
    public void remove(Billet billet)
    {   billets.remove(billet);

    }

    public List<String> toStrings()
    {
        ArrayList<String> strings=new ArrayList<>();
        for(Billet billet : billets)
        {   String s="seance: "+billet.getSeance().getId()+" "+billet.getSeance().getFilm().getTitre()+" date "+billet.getSeance().getDebut().toString();
            strings.add(s);

        }
        return strings;
    }

}
