package mbourdin.cinema_cours.api;


import mbourdin.cinema_cours.auxiliaire.PlayChamp;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Personne;
import mbourdin.cinema_cours.model.Play;

import mbourdin.cinema_cours.service.FilmManager;
import mbourdin.cinema_cours.service.PersonneManager;
import mbourdin.cinema_cours.service.PlayManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/play")
public class PlayRestController {
    private PlayManager playManager;
    private FilmManager filmManager;
    private PersonneManager personneManager;
    public PlayRestController(PlayManager playManager, FilmManager filmManager, PersonneManager personneManager){
        assert(playManager != null);
        this.playManager = playManager;
        assert (filmManager!=null);
        this.filmManager=filmManager;
        assert (personneManager!=null);
        this.personneManager=personneManager;
    }

    @GetMapping("/{id}")
    public Play getById(@PathVariable("id")long id){
        return playManager.getById(id);
    }

    @GetMapping("")
    public List<Play> getAll() {
        return playManager.getAll();
    }

    /**
     *
     * @param playChamp le Play a sauvegarder
     * @return identique au parametre play
     */


    @PostMapping("")
    public Play add(@RequestBody PlayChamp playChamp){
        Play play;
        if(playChamp.id==0) {
            play= new Play();
        }
        else
        {
            play=playManager.getById(playChamp.id);
            if(playChamp.personneId==null)
            {   playChamp.personneId=play.getPersonne().getId();

            }
        }
        Film film=filmManager.getById(playChamp.filmId);
        Personne personne=personneManager.getById(playChamp.personneId);
        play.setAll(film,personne,playChamp.numero,playChamp.nom);
        playManager.save(play);
        return play;
    }

    /**
     *
     * @param playChamp le Play a sauvegarder
     * @return identique au parametre play
     */
    @PutMapping("")
    public Play mod(@RequestBody PlayChamp playChamp){

        Play play=playManager.getById(playChamp.id);
        Film film=filmManager.getById(playChamp.filmId);
        Personne personne=personneManager.getById(playChamp.personneId);
        play.setAll(film,personne,playChamp.numero,playChamp.nom);
        return playManager.save(play);
    }

    /**
     *
     * @param playChamp le Play à deleteByUser
     * @return l'objet Play qui a été supprimé (ou non) de la base
     */
    @DeleteMapping("")
    public Play rm(@RequestBody PlayChamp playChamp){
         Play play=playManager.delete(playChamp.id);
         long id=play.getId();
         play=new Play();
         play.setId(id);
         return play;

    }

    /**
     *
     * @param id l'identifiant du play a deleteByUser
     * @return l'objet Genre qui a été supprimé de la base
     */
    @GetMapping("/rm/{id}")
    public Play rm(@PathVariable("id")long id){
        return playManager.delete(id);
    }
}
