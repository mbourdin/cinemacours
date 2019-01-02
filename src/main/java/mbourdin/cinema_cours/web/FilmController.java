package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.GenreDao;
import mbourdin.cinema_cours.dao.PersonneDao;
import mbourdin.cinema_cours.dao.PlayDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/film")
public class FilmController {
    @Autowired
    PersonneDao daoPersonne;

    @Autowired
    FilmDao daoFilm;

    @Autowired
    ImageManager imm;
    @Autowired
    PlayDao playDao;

    @Autowired
    GenreDao genreDao;

    @Value("${affichesPath}")
    String affichesPath;

    @GetMapping("/listParGenre/{id}")
    public String listeParGenre(Model m,@PathVariable Long id)
    {   Genre genre=genreDao.findById(id).get();
        m.addAttribute("choix","par genre : "+genre.getName() );
        m.addAttribute("title","liste des film par genre : "+genre.getName());
        m.addAttribute("listefilms",daoFilm.findAllByGenresContains(genre));
        return "film/liste";
    }


    @GetMapping("/{id}")
    public String detail(Model m, @PathVariable("id") String id){
        long idFilm=Long.parseLong(id);
        Film film= daoFilm.findById(idFilm).get();
        m.addAttribute("film",film);
        return "film/detail";
    }
    @GetMapping("/liste")
    public String listefilms(Model m){
        m.addAttribute("title","liste des films");
        m.addAttribute("listefilms",daoFilm.findAll());
        return "film/liste";
    }
    @GetMapping("/create")
    public String createFilm(Model m){
        m.addAttribute("title","creation film");
        m.addAttribute("film", new Film());
        m.addAttribute("personnes",daoPersonne.findAll());
        return "film/create";
    }
    @PostMapping("/create")
    public String createFilm(@ModelAttribute Film film, @RequestParam("image") MultipartFile file)
    {

        if(file.getContentType().equalsIgnoreCase("image/jpeg")){
            try {
                imm.savePoster(film, file.getInputStream());
            } catch (IOException ioe){
                System.out.println("Erreur lecture : "+ioe.getMessage());
            }
        }
        daoFilm.save(film);

        return "redirect:/film/liste";
    }
    @GetMapping("/update/{id}")
    public String updateFilm(Model m,@PathVariable("id") String id){
        Long idFilm=Long.parseLong(id);
        m.addAttribute("title","MAJ film");
        m.addAttribute("personnes",daoPersonne.findAll());
        m.addAttribute("film", daoFilm.findById(idFilm).get());
        return "/film/create";
    }

    @GetMapping("delete/{id}")
    public String deleteFilm(@PathVariable("id") String id){
        Long idFilm=Long.parseLong(id);
        daoFilm.delete(daoFilm.findById(idFilm).get());
        return "redirect:/film/liste";
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public @ResponseBody void getImage0(HttpServletResponse response, @PathVariable("id") String id)throws IOException, URISyntaxException {

        long idImage=Long.parseLong(id);
        String imagename= daoFilm.findById(idImage).get().getAfficheNom();
        BufferedImage image = ImageIO.read(new File(affichesPath+imagename));

        response.setContentType("image/jpg");
        OutputStream out;

        out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);

    }

}
