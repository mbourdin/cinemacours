package mbourdin.cinema_cours.service;


import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AuthenticationListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    HttpSession session;
    @Autowired
    CinemaUserService cinemaUserService;

    @Override
    public void onApplicationEvent( final AuthenticationSuccessEvent event) {
        System.out.println("loginEvent");
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        System.out.println("CONNEXION REUSSIE");
        String userName=userDetails.getUsername();
        Utilisateur user;

        user = cinemaUserService.findByLogin(userName);
        System.out.println(user);
        Utilities.setPermissions(session,user);
    }
}