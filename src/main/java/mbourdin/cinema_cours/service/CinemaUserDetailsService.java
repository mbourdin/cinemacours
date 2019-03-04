package mbourdin.cinema_cours.service;


import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.Utilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Projet thyme-security
 * Pour LAERCE SAS
 * <p>
 * Créé le  21/03/2017.
 *
 * @author fred
 */
@Service
public class CinemaUserDetailsService implements UserDetailsService {


    private UserDao userDao;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUser");
        Utilisateur user = userDao.findByLogin(username);
        log.info("Recherche utilisateur: "+username);
        if(user == null){
            throw new UsernameNotFoundException("Utilisateur introuvable : |"+username+"|");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.isActif())
        {   System.out.println("tests");
            System.out.println("type="+user.getType());

            switch (user.getType()) {
                case Utilisateur.vendeur:
                    System.out.println("vendeur");
                    authorities.add(new SimpleGrantedAuthority(Utilisateur.VENDEUR_RIGHTS));
                    break;
                case Utilisateur.admin:
                    System.out.println("admin");
                    authorities.add(new SimpleGrantedAuthority  (Utilisateur.VENDEUR_RIGHTS));
                    authorities.add(new SimpleGrantedAuthority(Utilisateur.ADMIN_RIGHTS));
                    authorities.add(new SimpleGrantedAuthority(Utilisateur.USER_RIGHTS));
                    break;
                case Utilisateur.normal:
                    System.out.println("normal");
                    authorities.add(new SimpleGrantedAuthority(Utilisateur.USER_RIGHTS));
                    break;
                default: throw new UsernameNotFoundException("Utilisateur sans droit : |"+username+"|");
            }
        }
        //System.out.println("actif="+user.isActif());
        //System.out.println("password="+user.getPassword());

        UserDetails userDetails=new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                authorities);
        System.out.println(userDetails);
        return userDetails;
    }
}
