package mbourdin.cinema_cours.service;




import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
public class Email {
    private static MailService mailService=new MailService();
    private static boolean inited=false;
    private static Properties props=System.getProperties();


    public static final String recoveryEmailTexte="Vous avez demandé la recupération de votre mot de passe sur le site Les Nanars Sauvages" +
            "\n si vous n'êtes pas à l'origine de cette demande veuillez ignorer ce message"+
            "\n pour vous connecter copiez-collez ce texte dans le champ de connexion à usage unique sur la page de connection:\n";

    public static final String validationEmailTexte="Vous venez de vous inscrire sur le site Les Nanars Sauvages"
            +"\n Pour activer votre compte veuillez cliquer sur le lien ci-dessous \n";

    private String texte;
    private String sujet;
    private InternetAddress[] adresses;
    public Email(String texte,String sujet,InternetAddress[] adresses) {

        this.texte = texte;
        this.adresses = adresses;
        this.sujet=sujet;
        if(!inited) {
                mailService.init();
            inited = true;
        }
    }

            //TODO envoyer un mail pour de vrai
            public void send() {   //TODO remplacer par un vrai email
            MimeMessage message=mailService.createMimeMessage();
                try{
                    message.setRecipients(Message.RecipientType.TO, adresses);
                    message.setText(texte);
                    message.setSubject(sujet);
                    mailService.send(message);

                    System.out.println("fin envoi mail");
                }catch (MessagingException e){System.out.println("echec d'envoi d'emails");};
                //System.out.println(texte);
            }
}
