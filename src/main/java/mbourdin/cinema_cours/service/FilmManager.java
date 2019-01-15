package mbourdin.cinema_cours.service;


import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.PlayDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.model.Play;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilmManager {
    @Autowired
    private FilmDao filmDao;

    @Autowired
    private PlayDao roleDao;

    FilmManager()
    {   this.filmDao = filmDao;
        assert(filmDao != null);
        this.roleDao=roleDao;
        assert(roleDao!=null);

    }
    public List<Film> getAllByGenre(Genre genre)
    {   return filmDao.findAllByGenresContains(genre);

    }

    public Film getById(long id){
        return filmDao.findById(id).get();
    }

    public List<Film> getAll(){
        return filmDao.findAllByOrderByTitreAsc();
    }

    /**
     * Sauvegarder le film
     * @param film le film à créer ou modifier
     * @return l'id du film créé ou modifié
     */
    public Long save(Film film){
        filmDao.save(film);
        return film.getId();
    }

    /**
     * effacer le film
     */
    public Film delete(Film film)
    {
        filmDao.delete(film);
                return film;
    }
    /**
     * Supprime un rôle dans un film
     * @param roleId l'identifiant du rôle à supprimer
     * @return l'id du film auquel appartenait le rôle
     */

    public long removeRole(long roleId){
        Play role = roleDao.findById(roleId).get();
        long filmId = role.getFilm().getId();
        Film film = filmDao.findById(filmId).get();
        film.getRoles().remove(role);
        filmDao.save(film);
        roleDao.delete(role);
        return filmId;
    }

    /**
     * Crée un role associé à un film
     * @param filmId l'identifiant du film
     * @param play le role à créer
     * @return l'id du film associé au rôle
     */
    public long addRole(long filmId, Play play){
        Film film = filmDao.findById(filmId).get();
        play.setFilm(film);
        roleDao.save(play);
        return play.getFilm().getId();
    }

    public long saveRole(Play play){
        roleDao.save(play);
        return play.getId();
    }
}