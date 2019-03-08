package mbourdin.cinema_cours.service;

import mbourdin.cinema_cours.model.Email;
import mbourdin.cinema_cours.model.NewsLetter;
import mbourdin.cinema_cours.model.Utilisateur;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class NewsThread extends Thread{
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private NewsLetter news;
    private Set<Utilisateur> utilisateurs;
    private MailSender sender;

    private SimpleMailMessage message;
    public NewsThread(NewsLetter news, Set<Utilisateur> utilisateurs, MailSender sender) {
        this.news = news;
        this.utilisateurs = utilisateurs;
        this.sender=sender;
        this.message=new SimpleMailMessage();
    }

    @Override
    public void run(){
        System.out.println("oui thread lancé!");
        Email email;
        for(Utilisateur utilisateur : utilisateurs)
        {       email=new Email(news.getTexte(),"Newsletter les nanars sauvages du "+ LocalDate.now().format(formatter),utilisateur.getEmail(),message);
                sender.send(email.getMessage());
        }

        System.out.println("thread terminé");
    }
}
