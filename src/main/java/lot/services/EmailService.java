package lot.services;

import io.github.cdimascio.dotenv.Dotenv;
import lot.exceptions.services.EmailException;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    private static final String SMTP_HOST;
    private static final int SMTP_PORT;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Dotenv dotenv = Dotenv.load();
        SMTP_HOST = dotenv.get("EMAIL_SMTP_HOST");
        SMTP_PORT = Integer.parseInt(dotenv.get("EMAIL_SMTP_PORT"));
        USERNAME = dotenv.get("EMAIL_USERNAME");
        PASSWORD = dotenv.get("EMAIL_PASSWORD");
    }

    public void sendConfirmationEmail(String recipientEmail, String reservationDetails) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);  // Ważne dla Gmaila!
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Potwierdzenie rezerwacji lotu");
            message.setText("Dziękujemy za rezerwację!\n\nSzczegóły:\n" + reservationDetails);

            Transport.send(message);
        }
        catch (MessagingException e) {
            throw new EmailException("Failed to send email to " + recipientEmail +
                    " via " + SMTP_HOST + ":" + SMTP_PORT, e);
        }
    }
}
