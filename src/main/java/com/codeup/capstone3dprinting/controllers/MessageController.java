package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
class MessageController {

    // These two next steps are often called dependency injection, where we create a Repository instance and initialize it in the controller class constructor.
    private final MessageRepository messageDao;
    private final UserRepository userDao;

    public MessageController(MessageRepository messageDao, UserRepository userDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
    }

    @GetMapping("/messages")
    public String index() {
        
        User user = userDao.findByIdEquals(1L);
        List<Message> list = messageDao.findByRecipientEquals(user);

        for (Message message: list) {
            System.out.println("message.getMessage() = " + message.getMessage());
        }
        
        return "messages/inbox";
    }

    @GetMapping("/messages/{id}")
    @ResponseBody
    public String viewMessage(@PathVariable Long id) {

        return id.toString() + " is the id";

    }
}