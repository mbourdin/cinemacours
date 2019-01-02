package mbourdin.cinema_cours.service;
import mbourdin.cinema_cours.model.Utilisateur;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public abstract class Utilities {
    public static String get_SHA_256_SecurePassword(String passwordToHash, String   salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static String get_SHA_512_SecurePassword(String passwordToHash, String   salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
    public static long parseIdRecoverLink(String s)
    {
        try{
        long result=Long.parseLong(s.substring(0,s.length()-64));
        return result;}catch (Exception e){e.printStackTrace();}
        return -1;
    }
    public static String parseLinkRecoverLink(String s)
    {   try{
        return s.substring(s.length()-64);}catch (Exception e){e.printStackTrace();}
        return "";
    }
    public static void setHPW(Utilisateur newuser,String password,String prefix,String suffix,String salt) {
        if (!password.equals("")) {
            String cleanPassword = prefix + password + suffix;
            String hpw = get_SHA_512_SecurePassword(cleanPassword, salt);
            newuser.setHpw(hpw);
        }
    }
    public static void setPermissions(HttpSession session,Utilisateur user)
    {   session.setAttribute("user",user);
        session.setAttribute("panier",new Panier());
        if (user.getType()==Utilisateur.admin)
        {   session.setAttribute("admin",Boolean.TRUE);
            session.setAttribute("vendeur",Boolean.TRUE);
        }
        if (user.getType()==Utilisateur.vendeur)
        {   session.setAttribute("vendeur",Boolean.TRUE);
        }

    }
}


