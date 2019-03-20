package mbourdin.cinema_cours.auxiliaire;

import mbourdin.cinema_cours.dao.SeanceDao;
import mbourdin.cinema_cours.model.Salle;
import mbourdin.cinema_cours.model.Seance;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Planning {
    public static final int MAXPIXELS=780;
    private List<Seance> seances;
    private Map<Seance,int[]> map;

    SeanceDao seanceDao;
    public Planning(Salle salle, LocalDateTime date, SeanceDao seanceDao)
    {   seances=seanceDao.getAllByDebutIsBeforeAndDebutIsAfterAndSalle(date.plusDays(1),date.minusMinutes(15),salle);
        map=new HashMap<>();
        for(Seance seance : seances)
        {   int[] bornes=new int[2];
            int debut=seance.getDebut().getHour()*60+seance.getDebut().getMinute()-600;
            int fin;
            if(seance.getFilm().getDuree()==null){seance.getFilm().setDuree(240);}
            fin=debut+seance.getFilm().getDuree();
            bornes[0]=debut;
            bornes[1]=fin;
            map.put(seance,bornes);
        }
    }
    public Map<Seance,int[]> getMap()
    {   return map;
    }
    public List<Seance> getSeances()
    {   return seances;

    }
}
