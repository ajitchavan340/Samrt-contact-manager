package com.dev.miceoservices.communication;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailSender {

    public boolean sendEmail(String to , String message ,String subject){

        String from = "testproject891@gmail.com";
        String host = "smtp.gmail.com";

        boolean f = false;

        Properties properties = System.getProperties();

        System.out.println("properties" + properties);

        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable",true);
        properties.put("mail.smtp.auth","true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,"SmartProject@1");
            }
        });
        session.setDebug(true);

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(from);
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
            f = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }
}
