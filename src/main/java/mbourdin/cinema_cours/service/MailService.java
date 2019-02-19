package mbourdin.cinema_cours.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
public class MailService extends JavaMailSenderImpl {
    static private String fromEmail;
    static private String smtpServer;
    static private Integer smtpPort;
    static private String sslPort;
    static private String emailPassword;
    static private String user;


    public MailService()
    {
    }
    public void init()
    {   this.setHost(smtpServer);
        this.setPassword(emailPassword);
        this.setPort(smtpPort);
        this.setUsername(user);
        Properties props=getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");//Enabling SMTP
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");



    }

    @Value("${fromEmail}")
    public void setFromEmail(String fromEmail) {
        MailService.fromEmail = fromEmail;
    }
    @Value("${smtpServer}")
    public void setSmtpServer(String smtpServer) {
        MailService.smtpServer = smtpServer;
    }
    @Value("${smtpPort}")
    public void setSmtpPort(Integer smtpPort) {
        MailService.smtpPort = smtpPort;
    }
    @Value("${sslPort}")
    public void setSslPort(String sslPort) {
        MailService.sslPort = sslPort;
    }
    @Value("${emailPassword}")
    public void setEmailPassword(String emailPassword) {
        MailService.emailPassword = emailPassword;
    }
    @Value("${user}")
    public void setUser(String username)
    {   MailService.user=username;

    }

    @Override
    public String toString()
    {   return fromEmail+":"+user+","+smtpServer+","+smtpPort+","+sslPort+","+emailPassword;

    }
}
