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
            email['__result'] = "FAIL"
            email['__errMSG'] << "Invalid JSON format"
            return JsonOutput.toJson(email)
        }


        try {
            def err = false;
            def emailPattern = /[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})/
            if (!email.to) {
                err = true
                email['__errMSG'] = email['__errMSG']? email['__errMSG'] : []
                email['__status'] = "FAIL"
                email['__errMSG'] << "Field 'to' cannot be empty"
            } else if (email.to.size() > 255) {
                err = true
                email['__errMSG'] = email['__errMSG']? email['__errMSG'] : []
                email['__status'] = "FAIL"
                email['__errMSG'] << "Field 'to' cannot be greather than 255 caracteres"
            } else if (email ==~ emailPattern) {
                err = true
                email['__errMSG'] = email['__errMSG']? email['__errMSG'] : []
                email['__status'] = "FAIL"
                email['__errMSG'] << "Field 'to' need to be a valid email format"
            }
            if (!email.subject) {
                err = true
                email['__errMSG'] = email['__errMSG']? email['__errMSG'] : []
                email['__status'] = "FAIL"
                email['__errMSG'] << "Field 'subject' cannot be empty"
            } else if (email.subject.size() > 255) {
                err = true
                email['__errMSG'] = email['__errMSG']? email['__errMSG'] : []
                email['__status'] = "FAIL"
                email['__errMSG'] << "Field 'subject' cannot be greather than 255 caracteres"
            }
            if (email.body && email.body.size() > 510) {
                err = true
                email['__errMSG'] = email['__errMSG']? email['__errMSG'] : []
                email['__status'] = "FAIL"
                email['__errMSG'] << "Field 'body' cannot be greather than 255 caracteres"
            }

            if(!err) {
                if (sendEmail(email)) {
                    email['__status'] = "SENDED"
                } else {
                    email['__errMSG'] = email['__errMSG']? email['__errMSG'] : []
                    email['__result'] = "FAIL"
                    email['__errMSG'] << "An error has occurred when sending email"
                }
            }

        } catch (Exception e) {
            e.printStackTrace()
            email['__errMSG'] = email['__errMSG']? email['__errMSG'] : []
            email['__result'] = "FAIL"
            email['__errMSG'] << "Invalid format"
        }
        return JsonOutput.toJson(email)
    }

    public static void main(String[] argv) {
        EmailService emailServer = new EmailService("EXCHANGE", "email");


    }


}






