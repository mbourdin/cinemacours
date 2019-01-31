package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.*;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Genre;
import mbourdin.cinema_cours.model.Play;
import mbourdin.cinema_cours.service.FilmManager;
import mbourdin.cinema_cours.service.GenreManager;
import mbourdin.cinema_cours.service.ImageManager;

import mbourdin.cinema_cours.service.TmdbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/film")
public class FilmController {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
    @Autowired
    PersonneDao daoPersonne;
    @Autowired
    FilmManager filmManager;

    @Autowired
    GenreManager genreManager;

    @Autowired
    ImageManager imm;
    @Autowired
    PlayDao playDao;

    @Autowired
    GenreDao genreDao;

    @Autowired
    ReviewDao reviewDao;

    @Value("${affichesPath}")
    String affichesPath;

    @Autowired
    TmdbClient tmdbClient;



    @GetMapping("/listParGenre/{id}")
    public String listeParGenre(Model m, @PathVariable Long id) {
        Genre genre = genreDao.findById(id).get();
        m.addAttribute("choix", "par genre : " + genre.getName());
        m.addAttribute("title", "liste des film par genre : " + genre.getName());
        m.addAttribute("listefilms", filmManager.getAllByGenre(genre));
        return "film/liste";
    }



    @GetMapping("/detail/{id}")
    public String detail(Model m, @PathVariable("id") Long id) {
        Film film = filmManager.getById(id);
        m.addAttribute("film", film);
        return "film/detail";
    }

    @GetMapping("/liste")
    public String listefilms(Model m) {
        m.addAttribute("title", "liste des films");
        m.addAttribute("listefilms", filmManager.getAll());
        return "film/liste";
    }

    @GetMapping("/create")
    public String createFilm(Model m) {
        m.addAttribute("title", "creation film");
        m.addAttribute("personnes", daoPersonne.findAll());
        m.addAttribute("readonly", Boolean.TRUE);
        m.addAttribute("newrole", new Play());
        m.addAttribute("film", new Film());
        return "film/create";
    }

    @PostMapping("/create")
    public String createFilm(@ModelAttribute Film film, @RequestParam("image") MultipartFile file, @RequestParam String titre, @RequestParam String resume, @RequestParam Long realisateur, @RequestParam double note) {
        String filename = "";
        if (file.getContentType().equalsIgnoreCase("image/jpeg")) {
            try {
                imm.savePoster(film, file.getInputStream());
            } catch (IOException ioe) {
                System.out.println("Erreur lecture : " + ioe.getMessage());
            }
        }
        if (film.getId() != 0) {
            filmManager.merge(film);
        } else {
            filmManager.save(film);
        }
        return "redirect:/film/liste";
    }

    @GetMapping("/update/{id}")
    public String updateFilm(Model m, @PathVariable("id") long id) {
        Film film = filmManager.getById(id);
        film.setRoles(playDao.findAllByFilm_IdOrderByNumeroAsc(film.getId()));
        m.addAttribute("title", "MAJ film");
        m.addAttribute("personnes", daoPersonne.findAll());
        m.addAttribute("film", film);
        m.addAttribute("newrole", new Play());
        return "/film/create";
    }

    @GetMapping("delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id) {

        filmManager.delete(filmManager.getById(id));
        return "redirect:/film/liste";
    }

    @GetMapping("/add")
    public String add(Model model) {
        Film film = new Film();
        filmManager.save(film);
        model.addAttribute("title", "Ajout d'un film");
        model.addAttribute("persons", daoPersonne.findAll());
        model.addAttribute("genres", genreManager.getAll());
        model.addAttribute("film", film);
        model.addAttribute("newrole", new Play());
        model.addAttribute("nouveau", true);

        return "film/form";
    }

    @GetMapping("/mod/{id}")
    public String mod(@PathVariable("id") long id, Model model) {
        Film film = filmManager.getById(id);
        model.addAttribute("title", film.getTitre() + " : modification");
        model.addAttribute("persons", daoPersonne.findAll());
        model.addAttribute("genres", genreManager.getAll());
        model.addAttribute("film", film);
        model.addAttribute("plays", film.getRoles());
        model.addAttribute("newrole", new Play());
        model.addAttribute("nouveau", false);
        return "film/form";
    }


    @PostMapping("/add")
    public String submit(@ModelAttribute Film film, @RequestParam String dateString, @RequestParam("image") MultipartFile file) {
        if (file.getContentType().equalsIgnoreCase("image/jpeg")) {
            try {
                imm.savePoster(film, file.getInputStream());
            } catch (IOException ioe) {
                System.out.println("Erreur lecture : " + ioe.getMessage());
            }
        }
        LocalDate date = LocalDate.parse(dateString, formatter);
        film.setAnnee(date);

        filmManager.save(film);


        return "redirect:/film/liste";
    }

    @GetMapping("/rmrole/{role_id}")
    public String rmRole(@PathVariable("role_id") Long roleId) {
        long filmId = filmManager.removeRole(roleId);

        return "redirect:/film/mod/" + filmId;
    }

    @PostMapping("/addrole")
    public String addRole(@ModelAttribute Play role) {
        long filmId = role.getFilm().getId();

        filmManager.addRole(filmId, role);
        return "redirect:/film/mod/" + filmId;
    }

    @PostMapping("/modrole/{id}")
    public String modRole(@PathVariable("id") long roleId, @ModelAttribute Play role) {
        filmManager.saveRole(role);
        return "redirect:/film/mod/" + role.getFilm().getId();
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public @ResponseBody
    void getImage0(HttpServletResponse response, @PathVariable("id") Long id) throws IOException, URISyntaxException {


        String imagename = filmManager.getById(id).getAfficheNom();
        BufferedImage image = ImageIO.read(new File(affichesPath + imagename));

        response.setContentType("image/jpg");
        OutputStream out;

        out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);

    }


    @GetMapping("/detailImport")
    public String importFilm(@RequestParam Long id) {
        Film film;

        try {
            tmdbClient.getMovieByTmdbId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/film/liste";
    }

    @GetMapping("/import")
    public String importer()
    {   return "/film/import";
    }


}
