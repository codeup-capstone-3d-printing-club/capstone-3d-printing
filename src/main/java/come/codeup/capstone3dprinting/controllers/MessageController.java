package come.codeup.capstone3dprinting.controllers;

import come.codeup.capstone3dprinting.repos.MessageRepository;
import come.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class MessageController {

    // These two next steps are often called dependency injection, where we create a Repository instance and initialize it in the controller class constructor.
    private final MessageRepository messageDao;

    public MessageController(MessageRepository messageDao) {
        this.messageDao = messageDao;
    }

    @GetMapping("/messages")
    @ResponseBody
    public String index() {

        return "messages index page";
    }
}