package mbourdin.cinema_cours.service;


import mbourdin.cinema_cours.dao.PersonneDao;
import mbourdin.cinema_cours.model.Personne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonneManager {
    @Autowired
    PersonneDao personneDao;

    public List<Personne> getAll(){
        return personneDao.findAllByOrderByName();
    }
    public Personne getById(Long id)
    {   Personne personne=personneDao.findById(id).orElseThrow(()->new IllegalArgumentException("Personne.id inexistant: "+id));
    return personne;
    }
}
