package com.codeup.capstone3dprinting.controllers;


import com.codeup.capstone3dprinting.models.ConfirmationToken;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.ConfirmationTokenRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import com.codeup.capstone3dprinting.services.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;


@Controller
public class AuthenticationController {

    private final EmailService emailService;
    private final ConfirmationTokenRepository tokenDao;
    private final UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    public AuthenticationController(EmailService emailService, ConfirmationTokenRepository tokenDao,
                                    UserRepository userDao, PasswordEncoder passwordEncoder) {

        this.emailService = emailService;
        this.tokenDao = tokenDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user, Model model){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setAvatarUrl("none");
        user.setAdmin(false);
        user.setVerified(false);
        user.setActive(true);
        user.setJoinedAt(new Timestamp(new Date().getTime()));

        User existingUserEmail = userDao.findByEmailIgnoreCase(user.getEmail());
        User existingUsername = userDao.findByUsernameIgnoreCase(user.getUsername());

        if (existingUserEmail != null || existingUsername != null) {

            // TODO: give the user a more detailed message about why account creation failed
            return "redirect:/login/?fail";

        } else {
            userDao.save(user);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            tokenDao.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("no-reply@squarecubed.xyz");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailService.sendEmail(mailMessage);

            model.addAttribute("email", user.getEmail());

            return "redirect:/login/?success";
        }
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(Model model, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = tokenDao.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userDao.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setVerified(true);
            userDao.save(user);
            return "home";
        }
        else
        {
            model.addAttribute("message","The link is invalid or broken!");
            return "home";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam(name = "currentPassword") String currentPassword,
                                 @RequestParam(name = "newPassword") String newPassword,
                                 @RequestParam(name = "confirmPassword") String confirmPassword)
    {
        System.out.println("currentPassword = " + currentPassword);
        System.out.println("newPassword = " + newPassword);
        System.out.println("confirmPassword = " + confirmPassword);
        return "redirect:/";
    }
}

