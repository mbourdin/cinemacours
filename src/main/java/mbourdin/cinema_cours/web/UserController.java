package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.Utilisateur;
import mbourdin.cinema_cours.service.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/* L'utilisateur connecté peut acceder
au détail et la mise a jour concernant
 UNIQUEMENT son propre compte*/
/* Toutes les autres fonctions de ce controlleur sont réservées a l'admin*/

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserDao userDao;
    @Value("${pwprefix}")
    String prefix;
    @Value("${pwsuffix}")
    String suffix;
    @Value("${salt}")
    String salt;
    @GetMapping("/detail/{id}")
    public String detail(Model m, @PathVariable("id") Long id){
        Utilisateur user= userDao.findById(id).get();
        m.addAttribute("user",user);
        m.addAttribute("title", "détail de l'utilisateur "+user.getId());
        return "user/detail";
    }

    @GetMapping("/liste")
    public String listeUsers(Model m){
        m.addAttribute("title","liste des utilisateurs");
        m.addAttribute("listeUsers",userDao.findAll());
        return "user/liste";
    }

    @GetMapping("/update/{id}")
    public String updateUser(Model m,@PathVariable("id") Long id){
        m.addAttribute("title","MAJ User");
        m.addAttribute("newuser", userDao.findById(id).get());
        return "/user/create";
    }
    @GetMapping("selfupdate")
    public String selfUpdateUser(Model m,@SessionAttribute("user") Utilisateur user){
        m.addAttribute("title","MAJ User");
        m.addAttribute("newuser", user);
        return "/user/create";
    }
    @GetMapping("/create")
    public String createUser(Model m)
    {   m.addAttribute("title","creation User");
        Utilisateur newuser=new Utilisateur();
        System.out.println(newuser);
        m.addAttribute("newuser",newuser);
        return "/user/create";
    }

    @PostMapping("/create")
    public String doCreateUser(@ModelAttribute Utilisateur newuser,@RequestParam("password") String password,@SessionAttribute("user") Utilisateur user)
    {   Utilities.setHPW(newuser,password,prefix,suffix,salt);
        if((user.equals(newuser)&&user.getType()==newuser.getType())
            || (user.getType()==Utilisateur.admin)
        )
        {   //empecher l'admin de se supprimer ses propres droits d'administration
            if (user.getType()==Utilisateur.admin&&user.equals(newuser)) newuser.setType(Utilisateur.admin);

            userDao.save(newuser);
        }
        return "redirect:/user/liste";
    }
    @GetMapping("delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id,@SessionAttribute("user") Utilisateur user){
        Utilisateur utilisateur=userDao.findById(id).get();
        System.out.println(user);
        System.out.println(utilisateur);
        //empecher l'admin de se supprimer lui-même!
        if(!user.equals(utilisateur)){
            userDao.delete(utilisateur);
        }


        return "redirect:/user/liste";
    }
}
