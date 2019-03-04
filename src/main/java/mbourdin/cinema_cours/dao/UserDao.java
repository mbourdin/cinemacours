package mbourdin.cinema_cours.dao;


import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao extends CrudRepository<Utilisateur,Long> {
    Utilisateur findByEmail(String email);
   Utilisateur findByLogin(String login);
    Set<Utilisateur> findAllByAbonneIsTrue();
    List<Utilisateur> findAll();
    boolean existsById(Long id);
}
