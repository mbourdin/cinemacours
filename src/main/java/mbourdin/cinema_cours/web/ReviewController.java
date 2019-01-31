package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.ReviewDao;
import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.Review;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    FilmDao filmDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ReviewDao reviewDao;

    @GetMapping("/listeParFilm/{filmId}")
    public String listeParFilm(Model m, @PathVariable("filmId") Long id){

        Film film= filmDao.findById(id).get();
        m.addAttribute("reviews",reviewDao.findAllByFilmAndValideIsTrue(film));
        return "review/liste";
    }
    @GetMapping("/listeParUtilisateur/{userId}")
    public String listeParUtilisateur(Model m, @PathVariable("userId") Long id){
        Utilisateur user= userDao.findById(id).get();
        m.addAttribute("reviews",user.getReviews());
        return "review/liste";
    }
    @GetMapping("/liste")
    public String listeParUtilisateur(Model m){

        m.addAttribute("reviews",reviewDao.findAll());
        return "review/liste";
    }
    @GetMapping("/new")
    public String nouveaux(Model m,@SessionAttribute Utilisateur user){
        Set<Review> reviews=reviewDao.findAllByValideIsFalse();
        m.addAttribute("reviews",reviews);
        return "review/liste";
    }

    @GetMapping("/create/{filmId}")
    public String formCreer(Model m, @PathVariable("filmId") Long id, HttpSession session){
        Film film= filmDao.findById(id).get();
        Utilisateur utilisateur=(Utilisateur) session.getAttribute("user");
        Review review=reviewDao.findByFilmAndUtilisateur(film,utilisateur);
        if(review==null) {
            review = new Review(film, utilisateur);
        }
        m.addAttribute("review",review);
        return "review/create";
    }
    @PostMapping("/create")
    public String creer(@ModelAttribute Review review,@SessionAttribute Utilisateur user){
        if (review.getUtilisateur().equals(user)||user.getType()==Utilisateur.admin) {
            reviewDao.save(review);
        }
        return "redirect:/film/detail/"+review.getFilm().getId();
    }
    @GetMapping("/{id}")
    public String reviewParId(Model m,@PathVariable("id") Long id){
        m.addAttribute("title","MAJ review");
        Review review=reviewDao.findById(id).get();
        m.addAttribute("review", review);
        return "review/create";
    }
    @GetMapping("/update/{id}")
    public String updateReview(Model m,@PathVariable("id") Long id){
        m.addAttribute("title","MAJ review");
        Review review=reviewDao.findById(id).get();
        m.addAttribute("review", review);
        return "review/create";
    }
    @GetMapping("delete/{id}")
    public String deleteReview(@PathVariable("id") Long id,HttpSession session){
        Review review=reviewDao.findById(id).get();
        Utilisateur utilisateur=(Utilisateur) session.getAttribute("user");
        if (review.getUtilisateur().equals(utilisateur)||utilisateur.getType()==Utilisateur.admin) {
            reviewDao.delete(review);
        }
        return "redirect:/review/liste";
    }
    @GetMapping("/valide/{id}")
    public String valideReview(Model m,@PathVariable("id") Long id){
        m.addAttribute("title","MAJ review");
        Review review=reviewDao.findById(id).get();
        review.setValide(true);
        reviewDao.save(review);
        return "redirect:/review/new";
    }
}
