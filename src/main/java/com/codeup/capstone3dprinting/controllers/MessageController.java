package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
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
    public String index(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = new User(user);

        List<Message> receivedList = messageDao.findByRecipientEquals(currentUser);
        List<Message> sentList = messageDao.findBySenderEquals(currentUser);

        model.addAttribute("received", receivedList);
        model.addAttribute("sent", sentList);

        return "messages/inbox";
    }

    @GetMapping("/messages/{id}")
    public String viewMessage(@PathVariable Long id, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = new User(user);
        Message message = messageDao.findMessageById(id);

        //redirects to inbox if message ID does not exist
        if (message == null) {
            return "redirect:/messages";
        }

        //if the user isn't either the sender or the recipient, then also redirect to inbox
        if (message.getSender().getId() != currentUser.getId() && message.getRecipient().getId() != currentUser.getId()) {
            return "redirect:/messages";
        }

        model.addAttribute("message", message);
        return "messages/view";
    }

    @GetMapping("/messages/compose")
    public String composeMessage(Model model) {

        Message message = new Message();
        User user = new User();
        model.addAttribute("message", message);
        model.addAttribute("user", user);

        return "messages/compose";
    }

    @PostMapping("/messages/send")
    public String sendMessage(@RequestParam(name = "recipient") String recipient,
                              @RequestParam(name = "message") String message) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = new User(user);

        User receiver = userDao.findByUsernameIgnoreCase(recipient);
        Message newMessage = new Message(message, new Timestamp(new Date().getTime()), receiver, currentUser);

        messageDao.save(newMessage);

        return "redirect:/messages/?sent";
    }

}