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

    private PasswordEncoder passwordEncoder;

    private final UserRepository userDao;
    private final FileRepository fileDao;
    private final SettingRepository settingDao;
    private final MessageRepository messageDao;
    private final EmailService emailService;

    public UserController(UserRepository userDao, FileRepository fileDao, EmailService emailService,
                          PasswordEncoder passwordEncoder, SettingRepository settingDao, MessageRepository messageDao) {
        this.userDao = userDao;
        this.fileDao = fileDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
        User currentUser = userDao.getOne(user.getId());

        //if already following, then stop following
        if (following) {
            currentUser.getUsers().removeIf(n -> n.getId() == id);

            //otherwise, follow the user
        } else {
            User followedUser = userDao.getOne(id);

            //sends a message notifying the user they have been followed if optional setting is on
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
            User currentUser = userDao.getOne(user.getId());

            for (User u : currentUser.getUsers()) {
                if (u.getId() == id) {
                    hasUser = true;
                    break;
                }
            }
            model.addAttribute("following", hasUser);
            model.addAttribute("feed", getFollowFeed());
            model.addAttribute("currentUser", currentUser);
        }

        User userDb = userDao.getOne(id);
        model.addAttribute("user", userDb);
        model.addAttribute("thisUsersFiles", fileDao.findAllByOwner_Id(id));
        model.addAttribute("favorites", userDb.getFavorites());
        model.addAttribute("follower", userDb.getFollowers());
        model.addAttribute("followed", userDb.getUsers());

        if (userDb.isPrivate() && !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)) {
            return "redirect:/privateProfile/" + userDb.getId();
        }
        return "users/profile";
    }

    @GetMapping("/privateProfile/{id}")
    public String showPrivateProfile(@PathVariable long id, Model model) {
        User userDb = userDao.getOne(id);
        model.addAttribute("user", userDb);
        return "users/privateProfile";
    }
    //this is used so once user logs in it redirects to the profile the user was trying to see
    @GetMapping("/privateRedirect/{id}")
    public String redirectToLogin(@PathVariable long id) {
        User userDb = userDao.getOne(id);
        return "redirect:/profile/" + id;
    }

    //helper function to return files of followed users
    private List<File> getFollowFeed() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());

        List<File> files = new ArrayList<>();

        for (User followed : currentUser.getUsers()) {
            List<File> list = fileDao.findAllByOwner(followed);
            files.addAll(list);
        }

        return files;
    }

    @GetMapping("/profile/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        User user = userDao.getOne(id);

        model.addAttribute("user", user);
        return "users/editProfile";
    }

    @PostMapping("/profile/{id}/edit")
    public String editProfile(@PathVariable long id, @ModelAttribute User userEdit) {
        User user = userDao.getOne(id);
        System.out.println("userEdit.isPrivate() = " + userEdit.isPrivate());

        user.setUsername(userEdit.getUsername());
        user.setFirstName(userEdit.getFirstName());
        user.setLastName(userEdit.getLastName());
        user.setEmail(userEdit.getEmail());
        user.setPrivate(userEdit.isPrivate());
        if(user.isPrivate()){
            List <File> userFiles= user.getFiles();
            for (File f : userFiles){
                f.setPrivate(true);
            }
        }
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

        //send user away if theyr'e not an admin
        if (!currentUser.isAdmin()) {
            return "redirect:/";
        }

        model.addAttribute("allAdmins", userDao.findAllByIsAdmin(true));
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
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        user.setActive(false);
        userDao.save(user);

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
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        user.setActive(true);
        userDao.save(user);

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

    //TODO: protect against directly calling the url to make changes, unless you are an admin
    @PostMapping("/users/{id}/makeAdmin")
    public String makeAdmin(@PathVariable long id) {
        User user = userDao.getOne(id);

        user.setAdmin(true);
        userDao.save(user);

        return "redirect:/admin";
    }

    //TODO: protect against directly calling the url to make changes, unless you are an admin
    @PostMapping("/users/{id}/removeAdmin")
    public String removeAdmin(@PathVariable long id) {
        User user = userDao.getOne(id);

        user.setAdmin(false);
        userDao.save(user);

        return "redirect:/admin";
    }
}

