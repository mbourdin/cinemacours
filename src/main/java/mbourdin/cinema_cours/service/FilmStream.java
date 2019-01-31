package mbourdin.cinema_cours.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.GZIPInputStream;

public  class FilmStream {

    public static final String tempfiles="C:\\cinemaTMP\\";


    private  static final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("MM_DD_YYYY");
        private BufferedReader br;
     public  BufferedReader getBr()
     { return br;
     }
     public FilmStream()
     {
         String adresse="http://files.tmdb.org/p/exports/movie_ids_"+ LocalDate.now().minusDays(1).format(formatter)+".json.gz";
         try {
             //sauvegarde du fichier
         InputStream httpIs = new URL(adresse).openStream();
            File file=new File(tempfiles+"films.7z");
            file.delete();
             Files.copy(httpIs, new File(tempfiles+"films.7z").toPath());
         //ouverture du fichier sauvegardé

        InputStream is=new FileInputStream(tempfiles+"films.7z");

         GZIPInputStream gzipIs = new GZIPInputStream(is);
             BufferedInputStream buffIs = new BufferedInputStream(gzipIs);
             InputStreamReader ir=new InputStreamReader(buffIs);
             br=new BufferedReader(ir);
        }catch (Exception e){
         e.printStackTrace();
         System.out.println("erreur a l'ouverture du stream");
     }
        System.out.println(adresse);
     }
}
