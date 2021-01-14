package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.*;
import com.codeup.capstone3dprinting.repos.*;
import com.codeup.capstone3dprinting.services.ReCaptchaValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
class FileController {

    private final FileRepository fileDao;
    private final CommentRepository commentDao;
    private final UserRepository userDao;
    private final RatingRepository ratingDao;
    private final CategoryRepository categoryDao;
    private final SettingRepository settingDao;
    private final MessageRepository messageDao;
    private final ImagesRepository imageDao;

    @Autowired
    private ReCaptchaValidationService validator;

    public FileController(FileRepository fileDao, CommentRepository commentDao, UserRepository userDao,
                          RatingRepository ratingDao, CategoryRepository categoryDao, SettingRepository settingDao,
                          MessageRepository messageDao, ImagesRepository imageDao) {
        this.fileDao = fileDao;
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.ratingDao = ratingDao;
        this.categoryDao = categoryDao;
        this.settingDao = settingDao;
        this.messageDao = messageDao;
        this.imageDao = imageDao;
    }

    @GetMapping("/files")
    public String showAllFiles(Model model) {
        HashMap<String, Integer> categoryAndFileNumber = new HashMap<>();
        for (Category categoryList : categoryDao.findAll()) {
            categoryAndFileNumber.put(capitalizeFirstLetter(categoryList.getCategory()), fileDao.findByCategories(categoryList).size());
        }
        model.addAttribute("categoryHashmap", categoryAndFileNumber);

        model.addAttribute("files", fileDao.findAll());
        model.addAttribute("pageTitle", "All Categories");
        model.addAttribute("totalFileNumber", fileDao.findAll().size());

        return "index";
    }

    public String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public List<Integer> getRatingsList(List<Rating> ListOfRatingObjs) {
        List<Integer> ratingNumbers = new ArrayList<>();
        for (Rating rating :
                ListOfRatingObjs) {
            ratingNumbers.add(rating.getRating());
        }
        return ratingNumbers;
    }

    @GetMapping("/files/{id}")
    public String showPost(@PathVariable long id, Model model) {
        File file = fileDao.getOne(id);
        List<FileImage> images = imageDao.getAllByFile_Id(id);
        List<Comment> thisFilesComments = commentDao.getAllByFile_Id(id);
        List<Rating> ListOfRatingObjs = ratingDao.getAllByFile_Id(id);
        List<Integer> thisFileRatings = getRatingsList(ListOfRatingObjs);

        boolean favorited = false;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = userDao.getOne(user.getId());
            for (File f : currentUser.getFavorites()) {
                if (f.getId() == id) {
                    favorited = true;
                    break;
                }
            }

            if (file.isPrivate() && currentUser.getId() != file.getOwner().getId()) {
                return "redirect:/privateFile/" + file.getId();
            }
        }
        model.addAttribute("favorited", favorited);
        model.addAttribute("imgFiles", images);
        model.addAttribute("averageRating", file.getAverageRating());
        model.addAttribute("allCommentsForThisPost", thisFilesComments);
        model.addAttribute("file", file);
        model.addAttribute("user", file.getOwner());
        if (file.isPrivate() && !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)) {
            return "redirect:/privateFile/" + file.getId();
        }
        System.out.println("SecurityContextHolder.getContext().getAuthentication().getPrincipal() = " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "files/showFile";
    }


    @GetMapping("/files/create")
    public String viewCreateForm(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());

        for (User follower : currentUser.getFollowers()) {
            System.out.println("follower.getId() = " + follower.getId());
        }

        model.addAttribute("file", new File());
        model.addAttribute("categoryList", categoryDao.findAll());

        return "files/createFile";
    }

    @PostMapping("/files/create")
    public String createPost(@ModelAttribute File fileToBeSaved,
                             @RequestParam(name = "categories", required = false) List<Long> newCategories,
                             @RequestParam(name = "g-recaptcha-response") String captcha, Model model) {
        // verify reCaptcha
        if (!validator.validateInvisibleCaptcha(captcha)) {
            model.addAttribute("message", "Please verify that you are not a robot.");
            return "files/createFile";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());

        fileToBeSaved.setCreatedAt(timestamp1);
        fileToBeSaved.setUpdatedAt(timestamp1);
        fileToBeSaved.setOwner(currentUser);
        fileToBeSaved.setAverageRating(0);

        List<Category> categoryList = new ArrayList<>();
        if (newCategories == null) {
            categoryList.add(categoryDao.findCategoryByCategory("other"));
        } else {
            for (long id : newCategories) {
                categoryList.add(categoryDao.getOne(id));
            }
        }
        fileToBeSaved.setCategories(categoryList);

        //triggers sending a message to users who follow the user who just posted the file
        for (User follower : currentUser.getFollowers()) {
            User receiver = userDao.getOne(follower.getId());

            if (receiver.getSettings().contains(settingDao.getOne(1L))) {

                Message newMessage = new Message(currentUser.getUsername() + " has posted a new file!",
                        new Timestamp(new Date().getTime()), receiver, userDao.getOne(1L));
                messageDao.save(newMessage);
            }
        }
        if (fileToBeSaved.getOwner().isPrivate()) {
            fileToBeSaved.setPrivate(true);
        }

        File dbFile = fileDao.save(fileToBeSaved);
        return "redirect:/files/" + dbFile.getId();
    }

    @GetMapping("/files/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        File file = fileDao.getOne(id);

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User userLoggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = userDao.getOne(userLoggedIn.getId());
            if (currentUser.getId() != userDao.findByFiles(file).getId()) {
                return "redirect:/files/" + file.getId();
            }
        }

        List<FileImage> images = imageDao.getAllByFile_Id(id);
        FileImage newImg = new FileImage();
        model.addAttribute("newImg", newImg);
        model.addAttribute("filesImgs", images);
        model.addAttribute("file", file);
        return "files/editFile";
    }

    @PostMapping("/files/{id}/edit")
    public String editFilePost(@PathVariable long id, @ModelAttribute File fileEdit) {
        File file = fileDao.getOne(id);

        file.setTitle(fileEdit.getTitle());
        file.setDescription(fileEdit.getDescription());
        file.setPrivate(fileEdit.isPrivate());
        fileDao.save(file);

        return "redirect:/files/" + id;
    }

    @PostMapping("/files/{id}/addImg")
    public String addImgToFile(@PathVariable long id, @RequestParam(name = "newImg") String imgURL) {
        File file = fileDao.getOne(id);
        FileImage newImg = new FileImage(fileDao.getOne(id), imgURL);

        file.addImg(newImg);
        fileDao.save(file);

        return "redirect:/files/" + id + "/edit";
    }

    @PostMapping("/files/{id}/removeImg/{imgID}")
    public String removeImgfromFile(@PathVariable long id, @RequestParam(name = "imgID") long imgID) {
        File file = fileDao.getOne(id);

        file.removeImg(imageDao.getOne(imgID));
        fileDao.save(file);

        return "redirect:/files/" + id + "/edit";
    }

    //    TODO:should redirect to admin dashboard if admin
    @PostMapping("/files/{id}/flag")
    public String flagUser(@PathVariable long id) {
        File file = fileDao.getOne(id);

        file.setFlagged(true);
        fileDao.save(file);

        return "redirect:/files/" + file.getId();
    }

    @PostMapping("/files/{id}/delete")
    public String deleteFilePost(@PathVariable long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        File file = fileDao.getOne(id);
        user = userDao.findByFiles(fileDao.getOne(id));

        if (currentUser.getId() != user.getId()) {
            return "redirect:/profile/" + currentUser.getId() + "?error";
        }

        file.setCategories(new ArrayList<>());
        fileDao.save(file);
        fileDao.delete(file);
//        TODO: redirect back to the list of your own file posts/ or admin dashboard if admin
        return "redirect:/profile/" + user.getId();
    }

    // user can only unflag as admin
    @PostMapping("/files/{id}/unflag")
    public String unflagUser(@PathVariable long id) {
        File file = fileDao.getOne(id);

        file.setFlagged(false);
        fileDao.save(file);

        return "redirect:/admin";
    }

    @PostMapping("/files/{id}/comment")
    @ResponseBody
    public String comment(@PathVariable long id, @RequestParam(name = "commentText") String commentText) throws JsonProcessingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        File file = fileDao.getOne(id);
        User fileOwner = userDao.getOne(file.getOwner().getId());

        if (fileOwner.getSettings().contains(settingDao.getOne(3L))) {
            Message newMessage = new Message(currentUser.getUsername() + " has commented on your file: " + file.getTitle(),
                    new Timestamp(new Date().getTime()), fileOwner, userDao.getOne(1L));
            messageDao.save(newMessage);
        }

        Comment newComment = new Comment();

        newComment.setComment(commentText);
        newComment.setCreatedAt(new Timestamp(new Date().getTime()));
        newComment.setFile(fileDao.getOne(id));
        newComment.setOwner(currentUser);
        commentDao.save(newComment);

        return "test"
    }

    @PostMapping("files/{id}/comment/{commentId}")
    public String replyAComment(@PathVariable long id, @PathVariable long commentId, @RequestParam(name = "replyText") String replyText) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        File file = fileDao.getOne(id);
        User fileOwner = userDao.getOne(file.getOwner().getId());
        Comment currentComment = commentDao.getOne(commentId);

        if (fileOwner.getSettings().contains(settingDao.getOne(3L))) {
            Message newMessage = new Message(currentUser.getUsername() + " has commented on your file: " + file.getTitle(),
                    new Timestamp(new Date().getTime()), fileOwner, userDao.getOne(1L));
            messageDao.save(newMessage);
        }

        Comment newComment = new Comment();
        newComment.setComment(replyText);
        newComment.setCreatedAt(new Timestamp(new Date().getTime()));
        newComment.setFile(fileDao.getOne(id));
        newComment.setOwner(currentUser);
        newComment.setParent(currentComment);
        commentDao.save(newComment);

        return "redirect:/files/" + file.getId();
    }

    @PostMapping("/files/{id}/comment/{commentId}/delete")
    @ResponseBody
    public void deleteComment(@PathVariable long id, @PathVariable(name = "commentId") long commentId) {

        List<Comment> children = commentDao.findByParent(commentDao.getOne(commentId));
        for (Comment child : children) {
            commentDao.deleteById(child.getId());
        }
        commentDao.deleteById(commentId);
    }

    @PostMapping("files/{id}/rating")
    public String rateFile(@PathVariable long id, @RequestParam(name = "ratings") int rating) {
        Rating newRating = new Rating();
        File file = fileDao.getOne(id);

        newRating.setRating(rating);
        newRating.setFile(fileDao.getOne(id));
        ratingDao.save(newRating);

        List<Rating> list = ratingDao.getAllByFile_Id(id);

        double newSum = file.getAverageRating() * (list.size() - 1) + newRating.getRating();

        System.out.println("file.getAverageRating() = " + file.getAverageRating());
        System.out.println("list.size() = " + list.size());
        System.out.println("newRating.getRating() = " + newRating.getRating());
        System.out.println("newSum / list.size() = " + newSum / list.size());

        file.setAverageRating(newSum / list.size());
        fileDao.save(file);

        return "redirect:/files/" + file.getId();
    }

    @PostMapping("files/favorite/{id}")
    public String favoritePost(@PathVariable long id,
                               @RequestParam(name = "favorited") boolean favorited) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());
        File thisFile = fileDao.getOne(id);

        if (favorited) {
            currentUser.getFavorites().removeIf(n -> n.getId() == id);
        } else {
            currentUser.getFavorites().add(thisFile);
        }
        userDao.save(currentUser);
        return "redirect:/files/" + id;
    }

    @GetMapping("files/search")
    public String search(@RequestParam(name = "search") String searchTerm, Model model) {
        List<File> searched = fileDao.findAllByDescriptionIsLike("%" + searchTerm + "%");
        List<File> searchedTitle = fileDao.findAllByTitleIsLike("%" + searchTerm + "%");
        List<User> searchedUsers = userDao.findAllByUsernameIsLike("%" + searchTerm + "%");

        for (User user : searchedUsers) {
            for (File file : user.getFiles()) {
                searched.remove(file);
                searched.add(0, file);
            }
        }

        for (File file : searchedTitle) {
            searched.remove(file);
            searched.add(0, file);
        }

        HashMap<String, Integer> categoryAndFileNumber = new HashMap<>();
        for (Category categoryList : categoryDao.findAll()) {
            categoryAndFileNumber.put(capitalizeFirstLetter(categoryList.getCategory()), 0);
        }

        for (File file: searched) {
            for (Category category: file.getCategories()) {
                String cat = category.getCategory();
                categoryAndFileNumber.put(cat, categoryAndFileNumber.get(cat) + 1);
            }
        }

        model.addAttribute("categoryHashmap", categoryAndFileNumber);
        model.addAttribute("files", searched);
        model.addAttribute("results", searched.size() + " Result" + (searched.size() == 1 ? " " : "s ") + "for '"
                + searchTerm + "'");
        model.addAttribute("pageTitle", "All Categories");
        model.addAttribute("totalFileNumber", searched.size());

        return "index";
    }

    @GetMapping("/privateFile/{id}")
    public String showPrivateFile(@PathVariable long id, Model model) {
        File fileDb = fileDao.getOne(id);
        model.addAttribute("file", fileDb);
        return "files/privateFile";
    }

    @GetMapping("/privateFileRedirect/{id}")
    public String redirectToLoginForFile(@PathVariable long id) {
        File fileDb = fileDao.getOne(id);
        return "redirect:/files/" + id;
    }
}
