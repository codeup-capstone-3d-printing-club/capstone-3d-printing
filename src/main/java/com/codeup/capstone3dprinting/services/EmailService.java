package com.codeup.capstone3dprinting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("mailService")
public class EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.emailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        emailSender.send(email);
    }

//    public void prepareAndSend(Ad ad, String subject, String body) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(from);
//        msg.setTo(ad.getOwner().getEmail());
//        msg.setSubject(subject);
//        msg.setText(body);
//
//        try{
//            this.emailSender.send(msg);
//        }
//        catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
//        }
//    }
}