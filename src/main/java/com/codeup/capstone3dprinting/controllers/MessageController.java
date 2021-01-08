package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
class MessageController {

    private final MessageRepository messageDao;
    private final UserRepository userDao;

    public MessageController(MessageRepository messageDao, UserRepository userDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
    }

    @GetMapping("/messages")
    public String index(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        List<Message> receivedList = messageDao.findByRecipientEquals(currentUser);
        List<Message> sentList = messageDao.findBySenderEquals(currentUser);

        model.addAttribute("received", receivedList);
        model.addAttribute("sent", sentList);

        return "messages/inbox";
    }

    @GetMapping("/messages/{id}")
    public String viewMessage(@PathVariable Long id, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
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
                              @RequestParam(name = "message") String message,
                              RedirectAttributes redir) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        User receiver = userDao.findByUsernameIgnoreCase(recipient.trim());

        //if the recipient doesn't exist, then return to the page with an error message
        //TODO: aid user in typing recipient name correctly on page
        if (receiver == null) {
            redir.addFlashAttribute("rec", recipient);
            redir.addFlashAttribute("msg", message);
            return "redirect:/messages/compose?error";
        }

        Message newMessage = new Message(message, new Timestamp(new Date().getTime()), receiver, currentUser);

        messageDao.save(newMessage);

        return "redirect:/messages/?sent";
    }
}
