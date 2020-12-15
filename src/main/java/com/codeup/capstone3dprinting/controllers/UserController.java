package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.FileRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/users/follow/{id}")
    public String followUser(@PathVariable long id,
                             @RequestParam(name = "following") boolean following) {

        User user = userDao.findByIdEquals(1L);

        if (following) {
            user.getUsers().remove(userDao.findByIdEquals(id));
        } else {
            user.getUsers().add(userDao.findByIdEquals(id));
        }

        userDao.save(user);

        return "redirect:/profile/" + id;
    }

    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable long id, Model model) {
        //assuming logged in as a hard-coded user
        User user = userDao.findByIdEquals(1L);

        List<File> feed = followFeed();

        User userdb = userDao.getOne(id);
        model.addAttribute("user", userdb);
        model.addAttribute("thisUsersFiles", fileDao.findAllByOwner_Id(id));
        model.addAttribute("following", user.getUsers().contains(userDao.findByIdEquals(id)));
        model.addAttribute("feed", feed);
        return "users/profile";
    }

    @GetMapping("/profile/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        User userdb = userDao.getOne(id);
        model.addAttribute("user", userdb);
        return "users/editProfile";
    }

    @PostMapping("/profile/{id}/edit")
    public String editProfile(@PathVariable long id, @ModelAttribute User userEdit) {
        User user = userDao.getOne(id);
        user.setUsername(userEdit.getUsername());
        user.setFirst_name(userEdit.getFirst_name());
        user.setLast_name(userEdit.getLast_name());
        user.setEmail(userEdit.getEmail());
        userDao.save(user);
        return "redirect:/profile/" + user.getId();
    }

    @PostMapping("/profile/{id}/changeAvatar")
    public String changeAvatar(@PathVariable long id, @RequestParam(name = "avatar") String avatarURL) {
        User user = userDao.getOne(id);
        user.setAvatar_url(avatarURL);
        userDao.save(user);
        return "redirect:/profile/" + user.getId();
    }

    public List<File> followFeed() {
        //assuming logged in as a hard-coded user
        User user = userDao.findByIdEquals(1L);

        List<User> users = user.getUsers();
        List<File> files = new ArrayList<>();

//        for (User followed: users) {
//            files.addAll(followed.getOwnedFiles());
//        }

        return files;
    }
}