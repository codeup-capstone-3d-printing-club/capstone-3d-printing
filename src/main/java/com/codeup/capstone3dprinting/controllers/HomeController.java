package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.Rating;
import com.codeup.capstone3dprinting.repos.FileRepository;
import com.codeup.capstone3dprinting.repos.RatingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import java.util.List;

@Controller
public class HomeController {
    private final RatingRepository ratingDao;
    private final FileRepository fileDao;

    public HomeController(RatingRepository ratingDao, FileRepository fileDao) {
        this.ratingDao = ratingDao;
        this.fileDao = fileDao;
    }

    @GetMapping("/")
    public String landingPage(Model model) {
        List<File> top5 = fileDao.getTop5();

        model.addAttribute("topRated", top5);

        return "home";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping("/FAQ")
    public String frequentlyAskedPage() {
        return "FAQ";
    }



}

