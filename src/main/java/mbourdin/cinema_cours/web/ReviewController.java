package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.ReviewDao;
import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.IllegalTransitionException;
import mbourdin.cinema_cours.model.Review;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Basic;
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
        m.addAttribute("reviews",reviewDao.findAllByFilmAndEtat(film,Review.PUBLIE));
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
        Set<Review> reviews=reviewDao.findAllByEtat(Review.NOUVEAU);
        m.addAttribute("reviews",reviews);
        return "review/liste";
    }
    @GetMapping("/mesCommentaires")
    public String mesCommentaires(Model m,@SessionAttribute Utilisateur user){
        Set<Review> reviews=reviewDao.findAllByUtilisateur(user);
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
        else
        {   return "/error/403";
        }
        m.addAttribute("action","create");
        m.addAttribute("review",review);
        return "review/create";
    }
    @PostMapping("/create")
    public String creer(@ModelAttribute Review review,@SessionAttribute Utilisateur user,RedirectAttributes attributes){
        if (   (review.getUtilisateur().equals(user))
                && (!reviewDao.existsByFilmAndUtilisateur(review.getFilm(),user))
        )
        {
            if (review.getArticle().length()==0 || review.getArticle().length()>1500)
            {   attributes.addFlashAttribute("flashMessage", "votre commentaire est vide, ou trop long, ne dépassez pas 1500 caractères");
                return "redirect:/film/detail/"+review.getFilm().getId();
            }
            try{reviewDao.save(review);
                attributes.addFlashAttribute("flashMessage", "l'ajout de votre commentaire sur "+review.getFilm().getTitre()+" a réussi, il sera visible une fois validé par la modération");
            }catch (Exception e)
            {//e.printStackTrace();
                attributes.addFlashAttribute("flashMessage", "l'ajout de votre commentaire a échoué");
            }
        }
        return "redirect:/film/detail/"+review.getFilm().getId();
    }
    @PostMapping("/update")
    public String updater(@ModelAttribute Review review,@SessionAttribute Utilisateur user,RedirectAttributes attributes){
        if (   (review.getUtilisateur().equals(user))
                &&(review.editable())
        )
        {
            if (review.getArticle().length()==0 || review.getArticle().length()>1500)
            {   attributes.addFlashAttribute("flashMessage", "votre commentaire est vide, ou trop long, ne dépassez pas 1500 caractères");
                return "redirect:/review/mesCommentaires";
            }
            try{review.editer();
                reviewDao.save(review);
                attributes.addFlashAttribute("flashMessage", "l'edition de votre commentaire sur "+review.getFilm().getTitre()+" a réussi, il sera visible une fois validé par la modération");
            }catch (Exception e)
            {//e.printStackTrace();
                attributes.addFlashAttribute("flashMessage", "l'edition de votre commentaire a échoué");
            }
            return "redirect:/review/mesCommentaires";
        }
        else
        {   return "/error/403";
        }
    }

    @GetMapping("/detail/{id}")
    public String reviewParId(Model m,@PathVariable("id") Long id){
        m.addAttribute("title","MAJ review");
        Review review=reviewDao.findById(id).get();
        m.addAttribute("review", review);
        return "review/detail";
    }
    @GetMapping("/update/{id}")
    public String updateReview(Model m,@PathVariable("id") Long id){
        m.addAttribute("title","MAJ review");
        Review review=reviewDao.findById(id).get();
        m.addAttribute("review", review);
        m.addAttribute("action","update");
        return "review/create";
    }

    @GetMapping("/valide/{id}")
    public String valideReview(@PathVariable("id") Long id,HttpSession session){
        Boolean admin=(Boolean)session.getAttribute("admin");
        if(admin==null) admin=Boolean.FALSE;
        if(admin) {
            Review review = reviewDao.findById(id).get();
            try{review.publier();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/new";
        }
        return "redirect:/error/403";
    }
    @GetMapping("/rejeter/{id}")
    public String rejectReview(@PathVariable("id") Long id,HttpSession session){
        Boolean admin=(Boolean)session.getAttribute("admin");
        if(admin==null) admin=Boolean.FALSE;
        if(admin) {
            Review review = reviewDao.findById(id).get();
            try{review.rejeter();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/new";
        }
        return "redirect:/error/403";
    }
    @GetMapping("/retenir/{id}")
    public String retenirReview(@PathVariable("id") Long id,HttpSession session){
        Boolean admin=(Boolean)session.getAttribute("admin");
        if(admin==null) admin=Boolean.FALSE;
        if(admin) {
            Review review = reviewDao.findById(id).get();
            try{review.retenirPourModif();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/new";
        }
        return "redirect:/error/403";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimerReview(@PathVariable("id") Long id,HttpSession session){
        Utilisateur user=(Utilisateur) session.getAttribute("user");
        Review review = reviewDao.findById(id).get();
        if(user.equals(review.getUtilisateur())) {
            try{review.supprimer();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/mesCommentaires";
        }
        return "redirect:/error/403";
    }
    @GetMapping("/abandonner/{id}")
    public String abandonnerReview(@PathVariable("id") Long id,HttpSession session){
        Utilisateur user=(Utilisateur) session.getAttribute("user");
        Review review = reviewDao.findById(id).get();
        if(user.equals(review.getUtilisateur())) {
            try{review.abandonner();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/mesCommentaires";
        }
        return "redirect:/error/403";
    }
}
