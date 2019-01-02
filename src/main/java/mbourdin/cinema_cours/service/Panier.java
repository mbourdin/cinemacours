package mbourdin.cinema_cours.service;

import mbourdin.cinema_cours.model.Billet;

import java.util.HashSet;
import java.util.Set;

public class Panier {
    Set<Billet> billets;

    public Panier(Set<Billet> billets) {
        this.billets = billets;
    }

    public Panier() {this.billets=new HashSet<Billet>();
    }

    public Set<Billet> getBillets() {
        return billets;
    }

    public void setBillets(Set<Billet> billets) {
        this.billets = billets;
    }
    public void add(Billet billet)
    {   billets.add(billet);

    }
    public void remove(Billet billet)
    {   billets.remove(billet);

    }
}
