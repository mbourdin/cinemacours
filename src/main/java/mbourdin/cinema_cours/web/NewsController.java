package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.NewsDao;
import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.NewsLetter;
import mbourdin.cinema_cours.model.Utilisateur;
import mbourdin.cinema_cours.service.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;


@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    NewsDao newsDao;
    @Autowired
    UserDao userDao;

    @GetMapping("/create")
    public String  createewsLetter(Model m)
    {
        NewsLetter news=new NewsLetter();
        news.setDate(LocalDate.now());
        m.addAttribute("news",news);
        return "/news/create";

    }
    @GetMapping("/update/{id}")
    public String updateNewsLette(Model m, @PathVariable Integer id)
    {   NewsLetter news=newsDao.findById(id).get();
        m.addAttribute("news",news);
        return "/news/create";

    }
    @PostMapping("/create")
    public String doCreateNewsletter(@ModelAttribute NewsLetter news,@SessionAttribute Boolean admin)
    {   if(admin!=null&&admin.equals(Boolean.TRUE))
        {   newsDao.save(news);
        }
        return "redirect:/news/liste";
    }
    @GetMapping("liste")
    public String listeNews(Model m)
    {   m.addAttribute("newslist",newsDao.findAll());
        return "/news/liste";
    }
    @GetMapping("/send/{id}")
    public String sendNL(@PathVariable Integer id,@SessionAttribute Boolean admin)
    {   if(admin!=null&&admin.equals(Boolean.TRUE))
        {   //TODO lancer cette fonction dans un thread séparé! , limiter son utilisation CPU
            NewsLetter news=newsDao.findById(id).get();
            Set<Utilisateur> utilisateurs=userDao.findAllByAbonneIsTrue();
            for(Utilisateur utilisateur : utilisateurs)
            {   new Email(news.getTexte(),utilisateur.getEmail()).send();
            }
        }
        return "redirect:/news/liste";
    }
}
