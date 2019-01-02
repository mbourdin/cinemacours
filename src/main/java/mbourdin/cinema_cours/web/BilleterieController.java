package mbourdin.cinema_cours.web;

import mbourdin.cinema_cours.dao.BilletDao;
import mbourdin.cinema_cours.dao.SeanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panier")
public class BilleterieController {
    @Autowired
    BilletDao billetDao;
    @Autowired
    SeanceDao seanceDao;
    @GetMapping("/commande/{id}")
    String commander(@PathVariable Long id, Model m)
    {   m.addAttribute("seance",seanceDao.findById(id).get());
        return "/panier/create";
    }

}
