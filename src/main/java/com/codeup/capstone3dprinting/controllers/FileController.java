package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.FileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
class FileController {

    // These two next steps are often called dependency injection, where we create a Repository instance and initialize it in the controller class constructor.
    private final FileRepository fileDao;

    public FileController(FileRepository fileDao) {
        this.fileDao = fileDao;
    }

    @GetMapping("/files")
    public String showAllFiles(Model model) {
        model.addAttribute("allEntries", fileDao.findAll());
        return "index";
    }

    @GetMapping("/files/{id}")
    public String showPost(@PathVariable long id, Model model) {
        File filedb = fileDao.getOne(id);
        model.addAttribute("file", filedb);
        model.addAttribute("user", filedb.getOwner());
        return "files/showFile";
    }

    @PostMapping("/files/{id}")
    public String postIndividual(@PathVariable long id, Model model) {
        File filedb = fileDao.getOne(id);
        model.addAttribute("file", filedb);
        model.addAttribute("user", filedb.getOwner());
        return "files/showFile";
    }
    @GetMapping ("/files/create")
    public String viewCreateForm(Model model) {
        model.addAttribute("file", new File());
        return "files/createFile";
    }

    @PostMapping("/files/create")
    public String createPost(@ModelAttribute File fileToBeSaved){
//       User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        fileToBeSaved.setOwner(user);
        File dbFile = fileDao.save(fileToBeSaved);
        return "redirect:/files/" + dbFile.getId();
    }

    @GetMapping("/files/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        File filedb = fileDao.getOne(id);
        model.addAttribute("file", filedb);
        return "files/editFile";
    }

    @PostMapping("/files/{id}/edit")
    public String editFilePost(@PathVariable long id, @ModelAttribute File fileEdit) {
        File file = fileDao.getOne(id);
        file.setTitle(fileEdit.getTitle());
        file.setDescription(fileEdit.getDescription());
        file.setPrivate(fileEdit.isPrivate());
        file.setImgUrl(fileEdit.getImgUrl());
        fileDao.save(file);
        return "redirect:/files/" + file.getId();
    }
    @PostMapping("/files/{id}/delete")
    public String deleteFilePost(@PathVariable long id) {
        fileDao.deleteById(id);
//        TODO: make this return back to the list of your own file posts
        return "redirect:/files";
    }
}
