package mbourdin.cinema_cours.service;

import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//cette classe sert de container pour etre utilisée dans le formulaire de création de séances
// on ne peut pas utiliser directement une Liste


public class SeanceList {


   private List<SeanceChamp> champs;

    public SeanceList() {
        champs=new ArrayList<SeanceChamp>();
    }

    public SeanceList(List<SeanceChamp> champs) {
        this.champs = champs;
    }
    @Bean
    public List<SeanceChamp> getChamps() {
        return champs;
    }

    public void setChamps(List<SeanceChamp> champs) {
        this.champs = champs;
    }
    public void add(SeanceChamp champ)
    {   champs.add(champ);

    }
}