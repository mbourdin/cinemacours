package mbourdin.cinema_cours.service;

import mbourdin.cinema_cours.model.NewsLetter;
import mbourdin.cinema_cours.model.Utilisateur;
import mbourdin.cinema_cours.web.SeanceController;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class NewsThread extends Thread{
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private NewsLetter news;
    private Set<Utilisateur> utilisateurs;

    public NewsThread(NewsLetter news, Set<Utilisateur> utilisateurs) {
        this.news = news;
        this.utilisateurs = utilisateurs;
    }

    @Override
    public void run(){
        System.out.println("oui thread lancé!");
        for(Utilisateur utilisateur : utilisateurs)
        {   InternetAddress[] addresses=new InternetAddress[1];
            try{
                InternetAddress address=new InternetAddress(utilisateur.getEmail());
                addresses[0]=address;
                new Email(news.getTexte(),"Newsletter les nanars sauvages du "+ LocalDate.now().format(formatter),addresses).send();
            }catch(AddressException e){System.out.println("adresse mail incorrecte :"+utilisateur.getEmail());}
        }

    }
}
