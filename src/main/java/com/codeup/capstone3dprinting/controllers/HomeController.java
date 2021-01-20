package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.*;
import com.codeup.capstone3dprinting.repos.FileRepository;
import com.codeup.capstone3dprinting.repos.ImagesRepository;
import com.codeup.capstone3dprinting.repos.RatingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {
    private final RatingRepository ratingDao;
    private final FileRepository fileDao;
    private final ImagesRepository imageDao;

    public HomeController(RatingRepository ratingDao, FileRepository fileDao, ImagesRepository imageDao) {
        this.ratingDao = ratingDao;
        this.fileDao = fileDao;
        this.imageDao = imageDao;
    }

    @GetMapping("/")
    public String landingPage( Model model) {
        List<File> top5 = fileDao.getTop5();



        model.addAttribute("topRated", top5);
//        model.addAttribute("imgFiles", images);
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

//    @GetMapping("/files/{id}")
//    public String showPost(@PathVariable long id, Model model) {
//        File file = fileDao.getOne(id);
//        List<FileImage> images = imageDao.getAllByFile_Id(id);
//        List<Comment> thisFilesComments = commentDao.getAllByFile_Id(id);
//        List<Rating> ListOfRatingObjs = ratingDao.getAllByFile_Id(id);
//        List<Integer> thisFileRatings = getRatingsList(ListOfRatingObjs);
//
//        boolean favorited = false;
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
//            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            User currentUser = userDao.getOne(user.getId());
//            for (File f : currentUser.getFavorites()) {
//                if (f.getId() == id) {
//                    favorited = true;
//                    break;
//                }
//            }
//
//            if (file.isPrivate() && currentUser.getId() != file.getOwner().getId()) {
//                return "redirect:/privateFile/" + file.getId();
//            }
//        }
//        model.addAttribute("favorited", favorited);
//        model.addAttribute("imgFiles", images);
//        model.addAttribute("averageRating", file.getAverageRating());
//        model.addAttribute("allCommentsForThisPost", thisFilesComments);
//        model.addAttribute("file", file);
//        model.addAttribute("user", file.getOwner());
//        if (file.isPrivate() && !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)) {
//            return "redirect:/privateFile/" + file.getId();
//        }
//        System.out.println("SecurityContextHolder.getContext().getAuthentication().getPrincipal() = " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        return "files/showFile";
//    }

}

