package mbourdin.cinema_cours.auxiliaire;

import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Salle;
import mbourdin.cinema_cours.model.Seance;
import mbourdin.cinema_cours.model.Tarif;
import mbourdin.cinema_cours.web.SeanceController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class SeanceChamp {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private Long seanceId;
    private Integer salleId;
    private Long filmId;
    private String debut;
    private Integer tarifId;

    public SeanceChamp() {
        seanceId=new Long(-1);
        debut=LocalDateTime.now().format(formatter);
    }

    public SeanceChamp(Long seanceId, Salle salle, Film film, LocalDateTime debut,Tarif tarif) {
        if (seanceId!=null) this.seanceId = seanceId; else this.seanceId=new Long(-1);
        if (salle!=null) this.salleId = salle.getId(); else this.salleId=new Integer(-1);
        if (film!=null) this.filmId = film.getId(); else this.filmId=new Long(-1);
        if (debut!=null) this.debut = debut.format(formatter); else this.debut=LocalDateTime.now().format(formatter);
        if (tarif!=null) this.tarifId = tarif.getId(); else this.tarifId=new Integer(-1);
    }
    public Long getSeanceId() {
        return seanceId;
    }
    public void setSeanceId(Long seanceId) {
        this.seanceId = seanceId;
    }
    public Integer getSalleId() {
            return salleId;
        }

        public void setSalleId(Integer salleId) {
            this.salleId = salleId;
        }

        public Long getFilmId() {
            return filmId;
        }

        public void setFilmId(Long filmId) {
            this.filmId = filmId;
        }

        public String getDebut() {
            return debut;
        }

        public void setDebut(String debut) {
            this.debut = debut;
        }

    public Integer getTarifId() {
        return tarifId;
    }

    public void setTarifId(Integer tarifId) {
        this.tarifId = tarifId;
    }

}
