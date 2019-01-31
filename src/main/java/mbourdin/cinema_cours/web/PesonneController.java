package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.PersonneDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Personne;
import mbourdin.cinema_cours.service.ImageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Set;

@Controller
@RequestMapping("/personne")
public class PesonneController {
    @Autowired
    PersonneDao daoPersonne;

    @Autowired
    FilmDao daoFilm;

    @Autowired
    ImageManager imm;

    @Value("${photoPath}")
    String photoPath;

    @GetMapping("/{id}")
    public String personne(Model m, @PathVariable("id") String id){
        Long idPersonne=Long.parseLong(id);

        m.addAttribute("personne",daoPersonne.findById(idPersonne).get());
        return "/personne/detail";
    }
    @GetMapping("/liste")
    public String personne(Model m){
        m.addAttribute("title","liste des personne");
        m.addAttribute("personnes",daoPersonne.findAll());
        return "/personne/liste";
    }

    @GetMapping("/create")
    public String createPersone(Model m){
        m.addAttribute("title","creation personne");
        m.addAttribute("personne",new Personne());
        return "/personne/create";
    }

    @PostMapping("/create")
    public String createPerson(@ModelAttribute Personne p,@RequestParam("image") MultipartFile file)
    {       if(file.getContentType().equalsIgnoreCase("image/jpeg")){
            try {
                imm.savePhoto(p, file.getInputStream());
            } catch (IOException ioe){
                System.out.println("Erreur lecture : "+ioe.getMessage());
            }
        }
        daoPersonne.save(p);
        return "redirect:/personne/liste";

    }


    @GetMapping("/update/{id}")
    public String updatePersone(Model m,@PathVariable("id") String id){
        Long idPersonne=Long.parseLong(id);
        m.addAttribute("title","MAJ personne");
        m.addAttribute("personne", daoPersonne.findById(idPersonne).get());
        return "/personne/create";
    }

    @GetMapping("delete/{id}")
    public String deletePersonne(@PathVariable("id") String id){
        Long idPersonne=Long.parseLong(id);
        daoPersonne.delete(daoPersonne.findById(idPersonne).get());
        return "redirect:/personne/liste";
    }
    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public @ResponseBody void getImage1(HttpServletResponse response, @PathVariable("id") String id)throws IOException, URISyntaxException {

        Long idImage=Long.parseLong(id);
        String imagename= daoPersonne.findById(idImage).get().getPhotopath();
        BufferedImage image = ImageIO.read(new File(photoPath+imagename));

        response.setContentType("image/jpg");
        OutputStream out;

        out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);

    }

    /*
    @GetMapping("refitpersons")
    public String refit()
    {
        Set<Personne> personnes=daoPersonne.findAll();
        for(Personne personne: personnes)
        {   personne.setName(personne.getPrenom()+" "+personne.getNom());
            daoPersonne.save(personne);
        }
        return "/index";
    }
    */
}
