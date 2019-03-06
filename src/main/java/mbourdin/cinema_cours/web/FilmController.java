package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.*;
import mbourdin.cinema_cours.model.*;
import mbourdin.cinema_cours.service.FilmManager;
import mbourdin.cinema_cours.service.GenreManager;
import mbourdin.cinema_cours.service.ImageManager;

import mbourdin.cinema_cours.service.TmdbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    @Autowired
    TmdbFilmDao tmdbFilmDao;



    @GetMapping("/listParGenre/{id}")
    public String listeParGenre(Model m, @PathVariable Long id) {
        Genre genre = genreDao.findById(id).get();
        m.addAttribute("choix", "par genre : " + genre.getName());
        m.addAttribute("title", "liste des film par genre : " + genre.getName());
        m.addAttribute("listefilms", filmManager.getAllByGenre(genre));
        return "film/liste";
    }



    @GetMapping("/detail/{id}")
    public String detail(Model m, @PathVariable("id") Long id, HttpSession session) {
        Film film = filmManager.getById(id);
        Utilisateur user=(Utilisateur) session.getAttribute("user");
        boolean commentable=true;
        if(     (user==null) ||
                (reviewDao.existsByFilmAndUtilisateur(film,user))
            )
        {   commentable=false;
        }
        film.setRoles(playDao.findAllByFilm_IdOrderByNumeroAsc(id));
        m.addAttribute("film", film);
        m.addAttribute("commentable",commentable);
        m.addAttribute("reviews",reviewDao.findAllByFilmAndEtatOrderByDateDesc(film,Review.PUBLISHED));
        return "film/detail";
    }

    @GetMapping("/liste")
    public String listefilms(Model m) {
        m.addAttribute("title", "liste des films");
        m.addAttribute("listefilms", filmManager.getAll());
        return "film/liste";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/create")
    public String createFilm(Model m) {
        m.addAttribute("title", "creation film");
        m.addAttribute("personnes", daoPersonne.findAll());
        m.addAttribute("readonly", Boolean.TRUE);
        m.addAttribute("newrole", new Play());
        m.addAttribute("film", new Film());
        return "/film/create";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public String createFilm(@ModelAttribute Film film, @RequestParam("image") MultipartFile file, @RequestParam String titre, @RequestParam String resume, @RequestParam Long realisateur, @RequestParam double note) {
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id) {

        filmManager.delete(filmManager.getById(id));
        return "redirect:/film/liste";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/rmrole/{role_id}")
    public String rmRole(@PathVariable("role_id") Long roleId) {
        long filmId = filmManager.removeRole(roleId);

        return "redirect:/film/mod/" + filmId;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addrole")
    public String addRole(@ModelAttribute Play role) {
        long filmId = role.getFilm().getId();

        filmManager.addRole(filmId, role);
        return "redirect:/film/mod/" + filmId;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/smallDetail/{id}")
    public String smallDetail(Model m,@PathVariable Long id)
    {   Film film=tmdbClient.getMovieSmallDetail(id);
        m.addAttribute("film",film);
        return "/film/smalldetail";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/detailImport")
    public String importFilm(@RequestParam Long id,RedirectAttributes attributes) {

        try {
            tmdbClient.getMovieByTmdbId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        attributes.addFlashAttribute("flashMessage", "le film "+filmManager.getByTmdbId(id).getTitre()+" a bien été ajouté");
        return "redirect:/film/liste";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/detailImport/{id}")
    public String importerFilm(@PathVariable Long id,RedirectAttributes attributes)
    {
        return importFilm(id,attributes);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/import")
    public String importer(Model m,@RequestParam String str)
    {   if(str.length()>=3)
        {
            List<TmdbFilm> films=tmdbFilmDao.findAllByTitleContainingIgnoreCase(str);
            m.addAttribute("films",films);
        }
        return "/film/import";
    }
}
