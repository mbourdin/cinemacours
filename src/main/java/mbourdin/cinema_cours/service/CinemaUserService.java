package mbourdin.cinema_cours.service;


import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Projet thyme-security
 * Pour LAERCE SAS
 * <p>
 * Créé le  21/03/2017.
 *
 * @author fred
 */
@Service
public class CinemaUserService {
    private UserDao userDao;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }



    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void saveWithNewPass(Utilisateur user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userDao.save(user);
    }
    public void saveWithSamePass(Utilisateur user){
        userDao.save(user);
    }

    public Utilisateur findByLogin(String login){
        return userDao.findByLogin(login);
    }

    public List<Utilisateur> listeUsers()
    {        return userDao.findAll();
    }
    public boolean exists(long id)
    {   return userDao.existsById(id);

    }

    public Utilisateur findByEmail(String mail)
    {   return userDao.findByEmail(mail);
    }
    public Utilisateur get(long id)
    {   return userDao.findById(id).get();
    }

}
