package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.File;
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

    @GetMapping("/files/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        File filedb = fileDao.getOne(id);
        model.addAttribute("file", filedb);
        return "files/editFile";
    }

    @PostMapping("/files/{id}/edit")
    public String editFilePost(@PathVariable long id, @ModelAttribute File fileEdit) {
        File file = fileDao.getOne(id);
        file.setFile_title(fileEdit.getFile_title());
        file.setDescription(fileEdit.getDescription());
        file.setIs_private(fileEdit.isIs_private());
        file.setImg_url(fileEdit.getImg_url());
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