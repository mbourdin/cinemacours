package mbourdin.cinema_cours.service;


import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Personne;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageManager {
    @Value("${imagepath}")
    String path;

    public int savePhoto(Personne p, InputStream fi){
        p.setPhotopath(save("p", "personnes", fi));
        return 0;
    }

    public int savePoster(Film f, InputStream fi) {
        f.setAfficheNom(save("f","affiches", fi));
        return 0;
    }

    private String getLastFile(String prefix, String dirPath){
        String max = "";
        try(DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(dirPath),prefix+"*")){
            for (Path fic: dir
                 ) {
                if(max.compareTo(fic.getFileName().toString())<0){
                    max = fic.getFileName().toString();
                }
                
            }
        }catch (IOException ioe){
            
        }
        return max;
    }

    private String save(String prefix, String subPath, InputStream fi){
        String fileName = "";
        try(DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(path+"/"+subPath),prefix+"*")){

            for (Path file: dir
            ) {
                if(fileName.compareTo(file.getFileName().toString())<0){
                    fileName = file.getFileName().toString();
                }
            }
            String numStr = fileName.substring(1, fileName.indexOf(".jpg"));
            System.out.println(numStr);
            Integer num = Integer.parseInt(numStr);
            numStr = String.format("%04d",num+1);
            System.out.println(numStr);
            fileName = prefix+numStr+".jpg";
            String filePath = path+"/"+subPath+"/"+fileName;
            Files.copy(fi, new File(filePath).toPath());
        }catch (IOException ioe){
            System.out.println("Erreur sur nom d'image : "+ioe.getMessage());
        }


        return fileName;
    }
}
