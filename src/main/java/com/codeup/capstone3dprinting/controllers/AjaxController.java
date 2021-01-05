package com.codeup.capstone3dprinting.controllers;


import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/ajax")
public class AjaxController {

    private final MessageRepository messageDao;
    private final UserRepository userDao;

    public AjaxController(MessageRepository messageDao, UserRepository userDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public void readMessage(@PathVariable(name = "id") String id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());

        Message message = messageDao.getOne(Long.parseLong(id));

        if (currentUser.getId() == message.getRecipient().getId()) {
            message.setUnread(false);
            messageDao.save(message);
        }

    }


}
