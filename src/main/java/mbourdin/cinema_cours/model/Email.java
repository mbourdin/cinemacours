package mbourdin.cinema_cours.model;




import org.springframework.mail.SimpleMailMessage;
public class Email {
    private SimpleMailMessage message;
    public SimpleMailMessage getMessage()
    {
        return message;
    }

    public static final String recoveryEmailTexte="Vous avez demandé la recupération de votre mot de passe sur le site Les Nanars Sauvages" +
            "\n si vous n'êtes pas à l'origine de cette demande veuillez ignorer ce message"+
            "\n pour vous connecter copiez-collez ce texte dans le champ de connexion à usage unique sur la page de connection:\n";

    public static final String validationEmailTexte="Vous venez de vous inscrire sur le site Les Nanars Sauvages"
            +"\n Pour activer votre compte veuillez cliquer sur le lien ci-dessous \n";

    public Email(String texte,String sujet,String address,SimpleMailMessage message) {

            this.message=message;
            message.setTo(address);
            message.setText(texte);
            message.setSubject(sujet);
    }
}
