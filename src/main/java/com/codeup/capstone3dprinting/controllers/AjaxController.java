package com.codeup.capstone3dprinting.controllers;


import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/ajax")
public class AjaxController {

    private final MessageRepository messageDao;
    private final UserRepository userDao;

    public AjaxController(MessageRepository messageDao, UserRepository userDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.POST)
    public String readMessage(@PathVariable(name = "id") String id, HttpSession session) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        List<Message> messages = new ArrayList<>();

        Message message = messageDao.getOne(Long.parseLong(id));

        if (currentUser.getId() == message.getRecipient().getId()) {
            message.setUnread(false);
            messageDao.save(message);
            messages = messageDao.findByRecipientAndUnread(currentUser, true);
            session.setAttribute("unread", messages.size());

        }
        return String.valueOf(messages.size());

    }

    @RequestMapping(value="/unread", method = RequestMethod.GET)
    public String getUnreadCount(HttpSession session) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());

        List<Message> messages = messageDao.findByRecipientAndUnread(currentUser, true);

        session.setAttribute("unread", messages.size());

        return String.valueOf(messages.size());
    }


}
