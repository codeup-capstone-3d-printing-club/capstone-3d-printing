package com.codeup.capstone3dprinting.services;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailService {

    @Value("${mailjet.public.key}")
    private String publicKey;

    @Value("${mailjet.secret.key}")
    private String privateKey;

    public void sendEmail(SimpleMailMessage message) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;

        ClientOptions options = ClientOptions.builder()
                .apiKey(publicKey)
                .apiSecretKey(privateKey)
                .build();

        client = new MailjetClient(options);

        String[] emailToArray = message.getTo();

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "no-reply@squarecubed.xyz")
                                        .put("Name", "squarecubed"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", Objects.requireNonNull(emailToArray)[0])))
                                .put(Emailv31.Message.SUBJECT, message.getSubject())
                                .put(Emailv31.Message.TEXTPART, message.getText())
                                .put(Emailv31.Message.CUSTOMID, "SquarecubedNotification")));
//                              .put(Emailv31.Message.HTMLPART, "<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!")
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());

    }
}
