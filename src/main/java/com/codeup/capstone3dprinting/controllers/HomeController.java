package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.Rating;
import com.codeup.capstone3dprinting.repos.FileRepository;
import com.codeup.capstone3dprinting.repos.RatingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String landingPage() {
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
//
//    @GetMapping("/topRated")
//    public String rating( Model model){
//        List<File> top5 = fileDao.findTop5ByRatingsOrderByRatings(ratingDao.findAll());
//        return "home";
//    }

}

