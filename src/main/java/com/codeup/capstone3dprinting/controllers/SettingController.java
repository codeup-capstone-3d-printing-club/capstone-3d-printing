package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.repos.SettingRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class SettingController {

    // These two next steps are often called dependency injection, where we create a Repository instance and initialize it in the controller class constructor.
    private final SettingRepository settingDao;

    public SettingController(SettingRepository settingDao) {
        this.settingDao = settingDao;
    }

    @GetMapping("/settings")
    @ResponseBody
    public String index() {

        return "users settings page";
    }

}
