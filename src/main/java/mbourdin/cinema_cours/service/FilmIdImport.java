package mbourdin.cinema_cours.service;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.TmdbFilmDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.TmdbFilm;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class FilmIdImport {
    @Autowired
    private TmdbFilmDao filmDao;
    private TmdbFilm film;
    public TmdbFilm importedFilm(String string)
    {
        JSONObject jo=new JSONObject(string);
        Long id=jo.getLong("id");
        String titreOriginal=jo.getString("original_title");
        Boolean adult=jo.getBoolean("adult");
        Optional<TmdbFilm> filmOptional=filmDao.findById(id);
        if(filmOptional.isPresent())
        {   film=filmOptional.get();
        }
        else {
            film = new TmdbFilm();
            film.setId(id);
            film.setTitle(titreOriginal);
        }
        if(adult) return null;

        return film;
    }
}
