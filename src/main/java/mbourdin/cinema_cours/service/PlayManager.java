package mbourdin.cinema_cours.service;


import mbourdin.cinema_cours.dao.PlayDao;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.model.Play;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Service pour la gestion du Genre des films
 */
@Component
public class PlayManager {

    /**
     * Le DAO qui gère le genre dans le système de persistance
     */
    private PlayDao playDao;


    /**
     * Constructeur utilisé par Spring pour la construction du bean
     * @param playDao le DAO qui gère le Play dans le système de persistance, ne peut être null
     */
    public PlayManager(PlayDao playDao){
        this.playDao = playDao;
        assert(playDao != null);
    }

    /**
     * Obtnir la liste de tous les genres dans la base
     * @return la liste des Play (par ordre alphabetique)
     */
    public List<Play> getAll(){
        return playDao.findAll();
    }

    /**
     * Récupère un genre par son identifiant
     * @param id identifiant du genre
     * @return le genre associé à l'id stocké dans la base
     * @throws IllegalArgumentException si id n'est pas dans la base
     */
    public Play getById(long id){
        return playDao.findById(id).orElseThrow(()->new IllegalArgumentException("Play.id inexistant: "+id));
    }

    /**
     * Sauvegarde ou crée (id=0) un genre
     * @param play le play à créer ou sauvegarder
     * @return le play sauvegardé ou créé
     */
    public Play save(Play play){
            return playDao.save(play);


    }

    /**
     * Supprime un genre de la base si et seulement si il existe et n'est lié à aucun film
     * @param id identitiant du genre à supprimer
     * @return le play détaché du système de persistance
     * @throws IllegalStateException si le genre est encore lié à au moins un film
     */
    public Play delete(long id){
        Play play=playDao.findById(id).get();
        playDao.deleteById(id);
        return play;
    }

}
