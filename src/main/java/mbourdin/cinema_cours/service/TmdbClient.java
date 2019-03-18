package mbourdin.cinema_cours.service;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.GenreDao;
import mbourdin.cinema_cours.dao.PersonneDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.model.Personne;
import mbourdin.cinema_cours.model.Play;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;


@Component
public class TmdbClient {
    @Autowired
    ImageManager imageManager;
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

    public void getImage(Personne personne)
    {   String adresse="http://image.tmdb.org/t/p/w500"+personne.getPhotopath();
        URL url;
        try{
            url=new URL(adresse);
            InputStream is=url.openStream();
            imageManager.savePhoto(personne,is);
        }catch (Exception e){e.printStackTrace();}
    }
    public void getImage(Film film)
    {   String adresse="http://image.tmdb.org/t/p/w500"+film.getAfficheNom();
        URL url;
        try{
            url=new URL(adresse);
            InputStream is=url.openStream();
            imageManager.savePoster(film,is);
        }catch (Exception e){e.printStackTrace();}
    }

    public Personne getPersonByTmdbId(long id) throws Exception
    {   RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;
        Long reset;

        String resourceUrl = "https://api.themoviedb.org/3/person/"+id+"?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceUrl, String.class);
        System.out.println(response.getBody());
        JSONObject person = new JSONObject(response.getBody());
        int nbReq= Integer.parseInt(stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
        System.out.println(nbReq+" requetes restantes");
        reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        if (nbReq==1)
        {   Thread.sleep(reset*1001);
        }
        Personne importedPerson;

        //Long tmdbId=person.getLong("id");

        String imagepath=null;
        if(!person.isNull("profile_path"))
        {   imagepath = person.getString("profile_path");
        }
        LocalDate annee=null;
        if(!person.isNull("birthday"))
        {   annee=LocalDate.parse( person.getString("birthday"));
        }
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
            importedPerson.setRoles(new ArrayList<Play>());
        }
        personneDao.save(importedPerson);
        return importedPerson;
    }
    public Film getMovieSmallDetail(Long id)
    {   RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;
        Long reset;

        String resourceUrl = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceUrl, String.class);
        System.out.println(response.getBody());
        int nbReq= Integer.parseInt(stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
        System.out.println(nbReq+" requetes restantes");
        reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        if (nbReq==1)
        {   try{Thread.sleep(reset*1001);}catch (Exception e){e.printStackTrace();}
        }
        System.out.println("Temps restant avant reset : "+reset+"\n\n");
        JSONObject film = new JSONObject(response.getBody());
        System.out.println("Titre : "+film.getString("title"));

        Film importedFilm=new Film();
        Long tmdbId=film.getLong("id");
        String imagepath=null;
        if(!film.isNull("poster_path")) {
            imagepath= film.getString("poster_path");
        }
        String titreOriginal=null;
        if(!film.isNull("original_title")) {
        titreOriginal=film.getString("original_title");
        }
        Double note=null;
        if(!film.isNull("vote_average")){
            note=film.getDouble("vote_average")/2;}
        LocalDate annee=null;
        if(!film.isNull("release_date")) {
            annee = LocalDate.parse(film.getString("release_date"));
        }
        String titre=null;
        if (!film.isNull("title")) {
            titre = film.getString("title");
        }
        String resume=null;
        if(!film.isNull("overview"))
        { resume=film.getString("overview");}

        Integer duree=null;
        if(!film.isNull("runtime")) {
            duree= film.getInt("runtime");
        }
        importedFilm.setTmdbId(tmdbId);
        importedFilm.setTitreOriginal(titreOriginal);
        importedFilm.setTitre(titre);
        importedFilm.setAnnee(annee);
        importedFilm.setResume(resume);
        importedFilm.setNote(note);
        importedFilm.setDuree(duree);
        importedFilm.setAfficheNom(imagepath);

        String resourceCredit = "https://api.themoviedb.org/3/movie/"+id+"/credits?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceCredit, String.class);
        JSONObject credit = new JSONObject(response.getBody());

        Personne acteur;
        Play play;
        JSONArray cast =credit.getJSONArray("cast");
        Personne realisateur=new Personne();
        JSONArray crew=credit.getJSONArray("crew");
        for (int i = 0; i < crew.length(); i++ ) {
            JSONObject job =crew.getJSONObject(i);
            String jobtitle=job.getString("job");
            if(jobtitle.equals("Director"))
            {   i=crew.length();
                 try{realisateur=getPersonByTmdbId(job.getLong("id"));}catch (Exception e){e.printStackTrace();}
                    getImage(realisateur);
                    importedFilm.setRealisateur(realisateur);
                    //personneDao.saveWithNewPass(realisateur); l'appel a la fonction précedente declanche déja une sauvegarde

                importedFilm.setRealisateur(realisateur);
            }
            System.out.println(job);
        }

        return importedFilm;
    }

    public void getMovieByTmdbId(long id) throws Exception {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;
        Long reset;
        Film importedFilm;
        String resourceUrl = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceUrl, String.class);
        System.out.println(response.getBody());
        int nbReq= Integer.parseInt(stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
        System.out.println(nbReq+" requetes restantes");
        reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        if (nbReq==1)
        {   Thread.sleep(reset*1001);
        }
        JSONObject film = new JSONObject(response.getBody());
        Long tmdbId=film.getLong("id");
        String imagepath=film.getString("poster_path");
        JSONArray genres=film.getJSONArray("genres");
        String titreOriginal=film.getString("original_title");
        Double note=film.getDouble("vote_average")/2;
        LocalDate annee=LocalDate.parse(film.getString("release_date"));
        String titre=film.getString("title");
        String resume=film.getString("overview");
        Integer duree=null;
        if(!film.isNull("runtime")) {
            duree= film.getInt("runtime");
        }
        System.out.println("Temps restant avant reset : "+reset+"\n\n");
        System.out.println("Titre : "+film.getString("title"));
        Optional<Film> filmOptional=filmDao.findByTmdbId(tmdbId);
        if(filmOptional.isPresent())
        {   importedFilm=filmOptional.get();
        }
        else {
            importedFilm = new Film();
            importedFilm.setGenres(new HashSet<Genre>());
            importedFilm.setRoles(new ArrayList<Play>());
        }
        importedFilm.setTmdbId(tmdbId);
        importedFilm.setTitreOriginal(titreOriginal);
        importedFilm.setTitre(titre);
        importedFilm.setAnnee(annee);
        importedFilm.setResume(resume);
        importedFilm.setNote(note);
        importedFilm.setDuree(duree);
        importedFilm.setAfficheNom(imagepath);

        getImage(importedFilm);// cela appelle la sauvegarde du film
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

        String resourceCredit = "https://api.themoviedb.org/3/movie/"+id+"/credits?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceCredit, String.class);
        JSONObject credit = new JSONObject(response.getBody());

        Personne acteur;
        Play play;
        JSONArray cast =credit.getJSONArray("cast");
        for (int i = 0; i < cast.length(); i++ ) {
            JSONObject role =cast.getJSONObject(i);
            String personnage=role.getString("character");
            long actorId=role.getLong("id");
            int order=role.getInt("order");
            acteur=personneDao.findByTmdbId(actorId);
            if(acteur==null)
            {   acteur=getPersonByTmdbId(actorId);
                getImage(acteur);
            }
            play=new Play();
            play.setPersonne(acteur);
            play.setFilm(importedFilm);
            play.setNumero(order);
            play.setNom(personnage);
            play.addPlay();
            //acteur.addFilm(importedFilm);
            personneDao.save(acteur);
            filmDao.save(importedFilm);

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
                    getImage(realisateur);
                    importedFilm.setRealisateur(realisateur);
                    filmDao.save(importedFilm);
                //personneDao.saveWithNewPass(realisateur); l'appel a la fonction précedente declanche déja une sauvegarde
                }
                importedFilm.setRealisateur(realisateur);
                filmDao.save(importedFilm);
            }
            System.out.println(job);
        }




    }
}
