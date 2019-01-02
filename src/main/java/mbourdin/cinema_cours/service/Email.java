package mbourdin.cinema_cours.service;

import mbourdin.cinema_cours.dao.UserDao;
import mbourdin.cinema_cours.model.Utilisateur;

public class Email {
    public static final String recoveryEmailTexte="Vous avez demandé la recupération de votre mot de passe sur le site Les Nanars Sauvages" +
            "\n si vous n'êtes pas à l'origine de cette demande veuillez ignorer ce message"+
            "\n pour vous connecter copiez-collez ce texte dans le champ de connexion à usage unique sur la page de connection:\n";

    public static final String validationEmailTexte="Vous venez de vous inscrire sur le site Les Nanars Sauvages"
            +"\n Pour acticer votre compte veuillez cliquer sur le lien ci-dessous \n";

    private String texte;
    private String adresse;
    public Email(String texte,String adresse)
    {   this.texte=texte;
    }
    //TODO envoyer un mail pour de vrai
    public void send()
    {   //TODO remplacer par un vrai email
        System.out.println(texte);
    }
}
