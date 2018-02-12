package Utils;

import java.util.*;
import javax.mail.*;

import javax.mail.internet.*;



public class EmailSender {
    final String senderEmailID = "ndreizampano@gmail.com";
    final String senderPassword = "fake";
    final String emailSMTPserver = "smtp.gmail.com";
    final String emailServerPort = "465";
    String receiverEmailID = null;
    static String emailSubject = "Test Mail javaa";
    static String emailBody = ":)";

    /*
        creates and sends an email
     */
    public EmailSender(String receiverEmailID, String Subject, String Body)
    {
        // Receiver Email Address
        this.receiverEmailID=receiverEmailID;
        // Subject
        this.emailSubject=Subject;
        // Body
        this.emailBody=Body;
        Thread t = new Thread(() ->
        {
            Properties props = new Properties();
            props.put("mail.smtp.user",senderEmailID);
            props.put("mail.smtp.host", emailSMTPserver);
            props.put("mail.smtp.port", emailServerPort);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.socketFactory.port", emailServerPort);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            SecurityManager security = System.getSecurityManager();
            try{
                SMTPAuthenticator auth = new SMTPAuthenticator();
                Session session = Session.getInstance(props, auth);
                MimeMessage msg = new MimeMessage(session);
                msg.setText(emailBody);
                msg.setSubject(emailSubject);
                msg.setFrom(new InternetAddress(senderEmailID));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmailID));
                Transport.send(msg);
                System.out.println("Message send Successfully:)");
            } catch (Exception mex){
                System.out.println(mex.getMessage());
            }
        });
        t.start();
    }

    public class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(senderEmailID, senderPassword); }
    }
}
