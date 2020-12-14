package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.FileRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class UserController {

    // These two next steps are often called dependency injection, where we create a Repository instance and initialize it in the controller class constructor.
    private final UserRepository userDao;
    private final FileRepository fileDao;

    public UserController(UserRepository userDao, FileRepository fileDao) {
        this.userDao = userDao;
        this.fileDao = fileDao;
    }

    @GetMapping("/users")
    @ResponseBody
    public String index() {

        return "users index page";
    }

    @GetMapping("/profile/{id}")
    @ResponseBody
    public String showProfile(@PathVariable long id, Model model) {
    User userdb = userDao.getOne(id);
        File files = fileDao.findByOwner(userdb);
    model.addAttribute("user", userdb);
    model.addAttribute("thisUsersFiles",files);
        return "profile";
    }


}