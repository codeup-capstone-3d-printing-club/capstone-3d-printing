package com.codeup.capstone3dprinting.controllers;


import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/ajax")
public class AjaxController {
    
    private final MessageRepository messageDao;

    public AjaxController(MessageRepository messageDao) {
        this.messageDao = messageDao;
    }


    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public String readMessage(@PathVariable(name = "id") String id) {

        Message message = messageDao.getOne(Long.parseLong(id));
        message.setUnread(false);
        messageDao.save(message);

        return "something";
    }


}
