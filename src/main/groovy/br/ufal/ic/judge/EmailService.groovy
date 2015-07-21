package br.ufal.ic.judge

import br.ufal.ic.judge.commons.ServerRPC
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;



class EmailService extends ServerRPC {
    EmailService(String exchangeName, String key) {
        super(exchangeName, key)
    }

    public boolean sendEmail(email) {
        final String username = "email.service.test123@gmail.com";
        final String password = "service123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            println session
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("email.service.test123@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email.to));
            message.setSubject(email.subject);
            message.setText(email.body);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
            return false;
        }
        return true;
    }

    String doWork (String message){
        def email;
        try {
            def jsonSlurper = new JsonSlurper()
            email = jsonSlurper.parseText(message)


        } catch(Exception e) {
            email['__result'] = "INVALID_FORMAT"
        }


        try {
            if(email.subject && email.body && email.to) {
                if (sendEmail(email)) {
                    email['__result'] = "SENDED"
                } else {
                    email['__result'] = "FAIL"
                }
            } else {
                email['__result'] = "INVALID_FORMAT"
            }

        } catch (Exception e) {
            e.printStackTrace()
            email['__result'] = "INVALID_FORMAT"
        }
        return JsonOutput.toJson(email)
    }

    public static void main(String[] argv) {
        EmailService emailServer = new EmailService("EXCHANGE", "email");


    }


}






