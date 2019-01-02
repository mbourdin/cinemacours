package mbourdin.cinema_cours.web;


        import mbourdin.cinema_cours.dao.UserDao;
        import mbourdin.cinema_cours.service.Email;
        import mbourdin.cinema_cours.model.Utilisateur;
        import mbourdin.cinema_cours.service.Utilities;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.*;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpSession;
        import java.util.Calendar;
        import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserDao userDao;
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

    @GetMapping("/connection")
    public String connection(Model m)
    {   m.addAttribute("title","connection");

        return "/login/connection";
    }
    @PostMapping("/connect")
    public String connect(HttpServletRequest request, HttpSession session)
    {   String login=request.getParameter("login");
        String password=request.getParameter("password");
        String cleanPassword=prefix+password+suffix;
        String hpw= Utilities.get_SHA_512_SecurePassword(cleanPassword,salt);
        Optional<Utilisateur> utilisateurOptional=userDao.findByLogin(login);
        boolean acceptConnection=(utilisateurOptional.isPresent())&&
                (utilisateurOptional.get().getHpw().equals(hpw));
        //System.out.println(acceptConnection);
        if(acceptConnection)
        {   Utilities.setPermissions(session,utilisateurOptional.get());
        }
        return "redirect:/";
    }
    @GetMapping("/recoverPW")
    public String recoverPW(Model m){
        m.addAttribute("title","récupération MDP");
        return "/login/recoverPW";
    }
    @PostMapping("/recover")
    public String sendRecoverEmail(HttpServletRequest request,Model m)
    {//TODO fonction qui envoie un mail contenant un lien de connection à usage unique
        //ne pas essayer de faire le getparameter dans le findbyemail,
        // cela conduit à un comportement des plus etranges,
        // du fait de la presence de plusieurs '.' dans une adress email
        String email=request.getParameter("email");
        Utilisateur user=userDao.findByEmail(email).get();
        user.generateLink();
        String link=user.getLink();
        userDao.save(user);
        String texte=Email.recoveryEmailTexte+user.getId()+link;
        Email mail=new Email(texte,email);
        mail.send();
        m.addAttribute("message","Un mail de connection a été envoyé à l'adresse : "+email);
        return connection(m);
    }
    @PostMapping("/subscribe")
    public String sendConfirmationEmail(@ModelAttribute Utilisateur newuser,@RequestParam("password") String password)
    {//TODO fonction qui envoie un mail contenant un lien de validation
        Utilities.setHPW(newuser,password,prefix,suffix,salt);
        newuser.setType(Utilisateur.normal);
        userDao.save(newuser);
        String link=Utilities.get_SHA_256_SecurePassword(Calendar.getInstance().getTimeInMillis()+"",newuser.getEmail());
        //mail mail=new Email(,user.getEmail());
        //mail.send();
        return "redirect:/";
    }
    @GetMapping("logout")
    public String logout(HttpServletRequest request)
    {   request.getSession().invalidate();
        return "redirect:/";
    }
    //login par lien a usage unique
    @PostMapping("/useLink")
    public String useRecoverLink(HttpServletRequest request,HttpSession session)
    {   String link=request.getParameter("link");
        long id=Utilities.parseIdRecoverLink(link);
        String lnk=Utilities.parseLinkRecoverLink(link);
        Utilisateur user=userDao.findById(id).get();
        boolean ok=lnk.equals(user.getLink());
        if (ok)
        {   user.setLink(null);
            userDao.save(user);
            Utilities.setPermissions(session,user);

        }
        return "redirect:/";
    }
}
