package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.*;
import com.codeup.capstone3dprinting.services.EmailService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final ConfirmationTokenRepository tokenDao;
    private final SettingRepository settingDao;
    private final MessageRepository messageDao;
    private final EmailService emailService;


    public UserController(UserRepository userDao, FileRepository fileDao, ConfirmationTokenRepository tokenDao,
                          EmailService emailService, Users users, PasswordEncoder passwordEncoder, SettingRepository settingDao, MessageRepository messageDao) {
        this.userDao = userDao;
        this.fileDao = fileDao;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenDao = tokenDao;
        this.settingDao = settingDao;
        this.messageDao = messageDao;
    }

    @GetMapping("/users")
    @ResponseBody
    public String index() {

        return "users index page";
    }

    @PostMapping("/users/follow/{id}")
    public String followUser(@PathVariable long id,
                             @RequestParam(name = "following") boolean following) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = new User(user);

        System.out.println("following = " + following);

        if (following) {
            currentUser.getUsers().removeIf(n -> n.getId() == id);
        } else {
            User followedUser = userDao.getOne(id);

            if (followedUser.getSettings().contains(settingDao.getOne(2L))) {
                Message newMessage = new Message(currentUser.getUsername() + " has followed you!",
                        new Timestamp(new Date().getTime()), followedUser, userDao.getOne(1L));
                messageDao.save(newMessage);
            }

            currentUser.getUsers().add(userDao.findByIdEquals(id));
        }

        userDao.save(currentUser);

        return "redirect:/profile/" + id;
    }

    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable long id, Model model) {

        boolean hasUser = false;

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = new User(user);

            for (User u : currentUser.getUsers()) {
                if (u.getId() == id) {
                    hasUser = true;
                    break;
                }
            }
            model.addAttribute("following", hasUser);
            model.addAttribute("feed", getFollowFeed());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("favorites", currentUser.getFavoriteFiles());
        }

        User userdb = userDao.getOne(id);
        model.addAttribute("user", userdb);
        model.addAttribute("thisUsersFiles", fileDao.findAllByOwner_Id(id));

        return "users/profile";
    }

    private List<File> getFollowFeed() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = new User(user);

        List<File> files = new ArrayList<>();

        for (User followed : currentUser.getUsers()) {
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        if(!currentUser.isAdmin()) {
            return "redirect:/";
        }

        model.addAttribute("allAdmins",userDao.findAllByIsAdmin(true));
        model.addAttribute("allUsers", userDao.findAllByisActive(true));
        model.addAttribute("allPosts", fileDao.findAll());
        model.addAttribute("flaggedUsers", userDao.findAllByisFlagged(true));
        model.addAttribute("flaggedPosts", fileDao.findAllByisFlagged(true));
        model.addAttribute("deactivatedUsers", userDao.findAllByisActive(false));
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

    //    redirects to admin bc nonAdmin users shouldn't be able to deactivate/activate users
    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable long id, RedirectAttributes redir) {
       User user = userDao.getOne(id);
       user.setActive(false);
       userDao.save(user);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("[squarecubed.xyz] Account deactivated");
        mailMessage.setFrom("no-reply@squarecubed.xyz");
        mailMessage.setText("Username: " + user.getUsername() + "'s account has been deactivated. Contact xyz for more info.");

        emailService.sendEmail(mailMessage);

        redir.addFlashAttribute("msg", "An email has been sent to " + user.getEmail() +
                " to let them know about account de-activation.");
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable long id, RedirectAttributes redir) {
        User user = userDao.getOne(id);
        user.setActive(true);
        userDao.save(user);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("[squarecubed.xyz] Account re-activated");
        mailMessage.setFrom("no-reply@squarecubed.xyz");
        mailMessage.setText("Username: " + user.getUsername() + "'s account has been re-activated.");

        emailService.sendEmail(mailMessage);

        redir.addFlashAttribute("msg", "An email has been sent to " + user.getEmail() +
                " to let them know about account re-activation.");
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/unflag")
    public String unflagUserAdmin(@PathVariable long id) {
        User user = userDao.getOne(id);
        user.setFlagged(false);
        userDao.save(user);
        return "redirect:/admin";
    }
    @PostMapping("/users/{id}/makeAdmin")
    public String makeAdmin(@PathVariable long id) {
        User user = userDao.getOne(id);
        user.setAdmin(true);
        userDao.save(user);
        return "redirect:/admin";
    }
    @PostMapping("/users/{id}/removeAdmin")
    public String removeAdmin(@PathVariable long id) {
        User user = userDao.getOne(id);
        user.setAdmin(false);
        userDao.save(user);
        return "redirect:/admin";
    }
}

