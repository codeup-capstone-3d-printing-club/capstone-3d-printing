package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.repos.FileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class FileController {

    // These two next steps are often called dependency injection, where we create a Repository instance and initialize it in the controller class constructor.
    private final FileRepository fileDao;

    public FileController(FileRepository fileDao) {
        this.fileDao = fileDao;
    }

    @GetMapping("/files")
    @ResponseBody
    public String index() {

        return "files index page";
    }

    @GetMapping("/file/1")
    public String show() {
        return "posts/show";
    }

}