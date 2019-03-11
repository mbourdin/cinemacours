package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.FilmDao;
import mbourdin.cinema_cours.dao.ReviewDao;
import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.Film;
import mbourdin.cinema_cours.model.IllegalTransitionException;
import mbourdin.cinema_cours.model.Review;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        m.addAttribute("reviews",reviewDao.findAllByFilmAndEtat(film,Review.PUBLISHED));
        return "review/liste";
    }
    @GetMapping("/listeParUtilisateur/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String listeParUtilisateur(Model m, @PathVariable("userId") Long id){
        Utilisateur user= userDao.findById(id).get();
        m.addAttribute("reviews",user.getReviews());
        return "review/liste";
    }
    @GetMapping("/liste")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String listeParUtilisateur(Model m){

        m.addAttribute("reviews",reviewDao.findAll());
        return "review/liste";
    }
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String nouveaux(Model m){
        Set<Review> reviews=reviewDao.findAllByEtat(Review.NEW);
        m.addAttribute("reviews",reviews);
        return "review/liste";
    }
    @GetMapping("/mesCommentaires")
    @PreAuthorize("hasAuthority('USER')")
    public String mesCommentaires(Model m,@SessionAttribute Utilisateur user){
        Set<Review> reviews=reviewDao.findAllByUtilisateur(user);
        m.addAttribute("reviews",reviews);
        return "review/liste";
    }
    @GetMapping("/create/{filmId}")
    @PreAuthorize("hasAuthority('USER')")
    public String formCreer(Model m, @PathVariable("filmId") Long id,@SessionAttribute Utilisateur user){
        Film film= filmDao.findById(id).get();
        Review review=reviewDao.findByFilmAndUtilisateur(film,user);
        if(review==null) {
            review = new Review(film, user);
        }
        else
        {   return "/error/403";
        }
        m.addAttribute("action","create");
        m.addAttribute("review",review);
        return "review/create";
    }
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
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
    @PreAuthorize("hasAuthority('USER')")
    public String updater(@ModelAttribute Review review,@SessionAttribute Utilisateur user,RedirectAttributes attributes){
        if (   (review.getUtilisateur().equals(user))
                &&(review.editable())
        )
        {
            if (review.getArticle().length()==0 || review.getArticle().length()>1500)
            {   attributes.addFlashAttribute("flashMessage", "votre commentaire est vide, ou trop long, ne dépassez pas 1500 caractères");
                return "redirect:/review/mesCommentaires";
            }
            try{review.edit();
                reviewDao.save(review);
                attributes.addFlashAttribute("flashMessage", "l'edition de votre commentaire sur "+review.getFilm().getTitre()+" a réussi, il sera visible une fois validé par la modération");
            }catch (Exception e)
            {//e.printStackTrace();
                attributes.addFlashAttribute("flashMessage", "l'edition de votre commentaire a échoué");
            }
            return "redirect:/review/mesCommentaires";
        }
        else
        {   return "error/403";
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
    @PreAuthorize("hasAuthority('USER')")
    public String updateReview(Model m,@PathVariable("id") Long id){
        m.addAttribute("title","MAJ review");
        Review review=reviewDao.findById(id).get();
        m.addAttribute("review", review);
        m.addAttribute("action","update");
        return "review/create";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/valide/{id}")
    public String valideReview(@PathVariable("id") Long id){
            Review review = reviewDao.findById(id).get();
            try{review.validByModerator();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/new";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/rejeter/{id}")
    public String rejectReview(@PathVariable("id") Long id){
            Review review = reviewDao.findById(id).get();
            try{review.reject();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/new";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/retenir/{id}")
    public String retenirReview(@PathVariable("id") Long id){

            Review review = reviewDao.findById(id).get();
            try{review.keepForEdit();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/new";
    }

    @GetMapping("/supprimer/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String supprimerReview(@PathVariable("id") Long id,@SessionAttribute Utilisateur user){
        Review review = reviewDao.findById(id).get();
        if(user.equals(review.getUtilisateur())) {
            try{review.deleteByUser();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/mesCommentaires";
        }
        return "redirect:/error/403";
    }

    @GetMapping("/abandonner/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String abandonnerReview(@PathVariable("id") Long id,@SessionAttribute Utilisateur user){
        Review review = reviewDao.findById(id).get();
        if(user.equals(review.getUtilisateur())) {
            try{review.abandon();}catch (IllegalTransitionException e){return "redirect:/error/403";}
            reviewDao.save(review);
            return "redirect:/review/mesCommentaires";
        }
        return "redirect:/error/403";
    }
}
