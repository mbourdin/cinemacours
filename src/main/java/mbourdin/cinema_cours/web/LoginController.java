package mbourdin.cinema_cours.web;


        import mbourdin.cinema_cours.service.CinemaUserService;
        import mbourdin.cinema_cours.service.Email;
        import mbourdin.cinema_cours.model.Utilisateur;
        import mbourdin.cinema_cours.service.Utilities;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.validation.annotation.Validated;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;
        import javax.mail.internet.AddressException;
        import javax.mail.internet.InternetAddress;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpSession;
        import javax.validation.constraints.NotBlank;

@Validated
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    CinemaUserService cinemaUserService;
    @Value("${pwprefix}")
    String prefix;
    @Value("${pwsuffix}")
    String suffix;
    @Value("${salt}")
    String salt;
    @GetMapping("/inscription")
    public String inscription(Model m){
        Utilisateur user=new Utilisateur();
        m.addAttribute("user",user);
        m.addAttribute("title","inscription");
        return "/login/inscription";
    }

    @GetMapping("")
    public String oldConnection(Model m)
    {   m.addAttribute("title","connection");
        m.addAttribute("old",true);
        return "/login/connection";
    }
    @GetMapping("/connect")
    public String connection(Model m)
    {   m.addAttribute("title","connection");
        m.addAttribute("old",false);
        return "/login/connection";
    }



    @GetMapping("/recoverPW")
    public String recoverPW(Model m){
        m.addAttribute("title","récupération MDP");
        return "/login/recoverPW";
    }

//    @PostMapping("/recover")
//    public String sendRecoverEmail(@javax.validation.constraints.Email @RequestParam  String email, RedirectAttributes attributes)
//    {
//        //ne pas essayer de faire le getparameter dans le findbyemail,
//        // cela conduit à un comportement des plus etranges,
//        // du fait de la presence de plusieurs '.' dans une adress email
//        Utilisateur user=cinemaUserService.findByEmail(email);
//        user.generateLink();
//        String link=user.getLink();
//        cinemaUserService.saveWithSamePass(user);
//        String texte=Email.recoveryEmailTexte+user.getId()+link;
//        try
//        {    InternetAddress adresse=new InternetAddress(email);
//            InternetAddress[] addresses=new InternetAddress[1];
//            addresses[0]=adresse;
//            Email mail=new Email(texte,"Recupération Mot de passe Les Nanars Sauvages",addresses);
//        mail.send();
//            attributes.addFlashAttribute("flashMessage", "Un mail de connection a été envoyé à l'adresse : "+email);
//        }catch (AddressException e)
//            {attributes.addFlashAttribute("flashMessage", "echec d'envoi du mail de récupération à l'adresse "+email);
//                e.printStackTrace();
//            }
//        //redirige vers la page de connexion, en affichant qu'un message a été envoyé
//        return "redirect:/login/connection";
//    }
//
    @PostMapping("/subscribe")
    public String sendConfirmationEmail(@ModelAttribute Utilisateur newuser,@RequestParam("password2") String password)
    {//TODO fonction qui envoie un mail contenant un lien de validation
        newuser.setType(Utilisateur.normal);
        System.out.println("\n"+password+"|"+newuser.getPassword());
        newuser.setPassword(password);
        cinemaUserService.saveWithNewPass(newuser);
        //String link=Utilities.get_SHA_256_SecurePassword(Calendar.getInstance().getTimeInMillis()+"",newuser.getEmail());
        //mail mail=new Email(,user.getEmail());
        //mail.send();
        return "redirect:/";
    }
}
