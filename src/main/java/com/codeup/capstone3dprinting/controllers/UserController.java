package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.FileRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import com.codeup.capstone3dprinting.repos.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
class UserController {

    private Users users;
    private PasswordEncoder passwordEncoder;

    // These two next steps are often called dependency injection, where we create a Repository instance and initialize it in the controller class constructor.
    private final UserRepository userDao;
    private final FileRepository fileDao;

    public UserController(UserRepository userDao, FileRepository fileDao, Users users, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.fileDao = fileDao;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setAvatarUrl("none");
        user.setAdmin(false);
        user.setVerified(false);
        user.setJoinedAt(new Timestamp(new Date().getTime()));

        users.save(user);
        return "redirect:/login";
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

        User userdb = userDao.getOne(id);
        model.addAttribute("user", userdb);
        model.addAttribute("thisUsersFiles", fileDao.findAllByOwner_Id(id));
        model.addAttribute("following", user.getUsers().contains(userDao.findByIdEquals(id)));
        model.addAttribute("feed", getFollowFeed());
        return "users/profile";
    }

    private List<File> getFollowFeed() {
        //assuming logged in as a hard-coded user
        User user = userDao.findByIdEquals(1L);
        List<File> files = new ArrayList<>();

        for (User followed : user.getUsers()) {
            List<File> list = fileDao.findAllByOwner(followed);
            files.addAll(list);
        }

        return files;

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
        user.setFirstName(userEdit.getFirstName());
        user.setLastName(userEdit.getLastName());
        user.setEmail(userEdit.getEmail());
        userDao.save(user);
        return "redirect:/profile/" + user.getId();
    }


    @PostMapping("/profile/{id}/changeAvatar")
    public String changeAvatar(@PathVariable long id, @RequestParam(name = "avatar") String avatarURL) {
        User user = userDao.getOne(id);
        user.setAvatarUrl(avatarURL);
        userDao.save(user);
        return "redirect:/profile/" + user.getId();
    }


    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
        model.addAttribute("allUsers", userDao.findAll());
        model.addAttribute("allPosts", fileDao.findAll());
        model.addAttribute("flaggedUsers", userDao.findAllByisFlagged(true));
        model.addAttribute("flaggedPosts", fileDao.findAllByisFlagged(true));
        return "admin/admin";
    }

    //TODO: needs to redirect back to user profile if not admin
    @PostMapping("/users/{id}/flag")
    public String flagUserAdmin(@PathVariable long id) {
        User user = userDao.getOne(id);
        user.setFlagged(true);
        userDao.save(user);
        return "redirect:/admin";
    }

    //    redirects to admin bc nonAdmin users shouldn't be able to delete users
    @PostMapping("/users/{id}/delete")
    public String deleteUserAsAdmin(@PathVariable long id) {
        userDao.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/unflag")
    public String unflagUserAdmin(@PathVariable long id) {
        User user = userDao.getOne(id);
        user.setFlagged(false);
        userDao.save(user);
        return "redirect:/admin";
    }
}

