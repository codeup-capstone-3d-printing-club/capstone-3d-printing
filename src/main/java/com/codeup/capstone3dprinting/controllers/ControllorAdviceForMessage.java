package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice("com.codeup.capstone3dprinting")
public class ControllorAdviceForMessage {

    private MessageRepository messageDao;
    private UserRepository userDao;

    public ControllorAdviceForMessage(MessageRepository messageDao, UserRepository userDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
    }

    @ModelAttribute
    public void populateMesssageModel(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("unread", null);
        } else {
            System.out.println("principal = " + principal.getName());
            User currentUser = userDao.findByUsernameIgnoreCase(principal.getName());
            List<Message> receivedList = messageDao.findByRecipientEquals(currentUser);

            List<Message> unread = new ArrayList<>();
            for (Message mes : receivedList) {
                if (mes.isUnread()) {
                    unread.add(mes);
                }
            }

            model.addAttribute("unread", unread);
        }
    }

}
