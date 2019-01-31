package mbourdin.cinema_cours.service;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.GenreDao;
import mbourdin.cinema_cours.dao.PersonneDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.model.Personne;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;


@Component
public class TmdbClient {
    @Autowired
    FilmDao filmDao;
    @Autowired
    GenreDao genreDao;
    @Autowired
    PersonneDao personneDao;
    @Value("${apikey}")
    private String apiKey;

    private long secondsBeforeReset(String value){
        long timestamp = Long.valueOf(stripBraces(value));
        LocalDateTime resetTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        return now.until( resetTime, ChronoUnit.SECONDS);
    }

    private String stripBraces(String value){
        return value.substring(0, value.length()-1).substring(1);
    }

    public Personne getPersonByTmdbId(long id) throws Exception
    {   RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;
        long reset;
        String resourceUrl = "https://api.themoviedb.org/3/person/"+id+"?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceUrl, String.class);
        System.out.println(response.getBody());
        JSONObject person = new JSONObject(response.getBody());
        Personne importedPerson;

        //Long tmdbId=person.getLong("id");
        String imagepath=person.getString("profile_path");

        LocalDate annee=LocalDate.parse(person.getString("birthday"));
        String name=person.getString("name");
        Personne personne=personneDao.findByTmdbId(id);
        if(personne!=null)
        {   importedPerson=personne;
        }
        else
        {   importedPerson=new Personne();
            importedPerson.setTmdbId(id);
            importedPerson.setNaissance(annee);
            importedPerson.setName(name);
            importedPerson.setPhotopath(imagepath);
        }
        personneDao.save(importedPerson);
        return importedPerson;
    }
    public void getMovieByTmdbId(long id) throws Exception {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;
        long reset;

        String resourceUrl = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceUrl, String.class);
        System.out.println(response.getBody());
        JSONObject film = new JSONObject(response.getBody());
        System.out.println("Titre : "+film.getString("title"));

        Film importedFilm;


        Long tmdbId=film.getLong("id");
        String imagepath=film.getString("poster_path");
        JSONArray genres=film.getJSONArray("genres");
        String titreOriginal=film.getString("original_title");
        Double note=film.getDouble("vote_average")/2;
        LocalDate annee=LocalDate.parse(film.getString("release_date"));
        String titre=film.getString("title");
        String resume=film.getString("overview");
        Integer duree=film.getInt("runtime");

        Optional<Film> filmOptional=filmDao.findByTmdbId(tmdbId);
        if(filmOptional.isPresent())
        {   importedFilm=filmOptional.get();
            importedFilm.setAfficheNom(imagepath);
        }
        else {
            importedFilm = new Film();
            importedFilm.setGenres(new HashSet<Genre>());

        }
        importedFilm.setTmdbId(tmdbId);
        importedFilm.setTitreOriginal(titreOriginal);
        importedFilm.setTitre(titre);
        importedFilm.setAnnee(annee);
        importedFilm.setResume(resume);
        importedFilm.setNote(note);
        importedFilm.setDuree(duree);
        Genre importedgenre;
        for(int i = 0; i < genres.length(); i++){
            JSONObject genre =genres.getJSONObject(i);
            Long genreId=genre.getLong("id");
            importedgenre=genreDao.findByTmdbId(genreId);
            if(importedgenre==null) {
                importedgenre = new Genre();
                importedgenre.setTmdbId(genreId);
                importedgenre.setName(genre.getString("name"));
                importedgenre.setFilms(new HashSet<Film>());
                importedgenre=genreDao.save(importedgenre);
                System.out.println(importedgenre);
            }
            importedFilm.addGenre(importedgenre);
            System.out.println("- Genre : "+genre.getString("name"));
        }


        System.out.println(importedFilm);
        filmDao.save(importedFilm);

        System.out.println("--------\nRequetes restantes : "+stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
        reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        System.out.println("Temps restant avant reset : "+reset+"\n\n");


        String resourceCredit = "https://api.themoviedb.org/3/movie/"+id+"/credits?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceCredit, String.class);
        JSONObject credit = new JSONObject(response.getBody());


        JSONArray cast =credit.getJSONArray("cast");
        for (int i = 0; i < cast.length(); i++ ) {
            JSONObject role =cast.getJSONObject(i);
            System.out.println(role.getString("name")+" joue "+ role.getString("character"));
        }
        Personne realisateur=null;
        JSONArray crew=credit.getJSONArray("crew");
        for (int i = 0; i < crew.length(); i++ ) {
            JSONObject job =crew.getJSONObject(i);
            String jobtitle=job.getString("job");
            if(jobtitle.equals("Director"))
            {   i=crew.length();
                realisateur=personneDao.findByTmdbId(job.getLong("id"));
                if(realisateur==null)
                {   realisateur=getPersonByTmdbId(job.getLong("id"));

                }
                personneDao.save(realisateur);
            }
            System.out.println(job);
        }
        importedFilm.setRealisateur(realisateur);
            filmDao.save(importedFilm);
        System.out.println("--------\nRequetes restantes : "+stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
        reset = reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        System.out.println("Temps restant avant reset : "+reset+"\n\n");
    }
}
