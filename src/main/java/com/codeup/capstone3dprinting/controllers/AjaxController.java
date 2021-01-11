package com.codeup.capstone3dprinting.controllers;


import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.FileRepository;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/ajax")
public class AjaxController {

    private final MessageRepository messageDao;
    private final UserRepository userDao;
    private final FileRepository fileDao;

    public AjaxController(MessageRepository messageDao, UserRepository userDao, FileRepository fileDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
        this.fileDao = fileDao;
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.POST)
    public String readMessage(@PathVariable(name = "id") String id, HttpSession session) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());

        Message message = messageDao.getOne(Long.parseLong(id));

        if (currentUser.getId() == message.getRecipient().getId()) {
            message.setUnread(false);
            messageDao.save(message);
        }

        String unread = String.valueOf(messageDao.findByRecipientAndUnread(currentUser, true).size());

        session.setAttribute("unread", unread);

        return unread;

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
