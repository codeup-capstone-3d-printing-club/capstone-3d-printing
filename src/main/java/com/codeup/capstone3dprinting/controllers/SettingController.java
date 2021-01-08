package com.codeup.capstone3dprinting.controllers;

import com.codeup.capstone3dprinting.models.Setting;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.SettingRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
class SettingController {

    private final SettingRepository settingDao;
    private final UserRepository userDao;

    public SettingController(SettingRepository settingDao, UserRepository userDao) {
        this.settingDao = settingDao;
        this.userDao = userDao;
    }

    @PostMapping("/settings")
    @ResponseBody
    public String updateSettings(@RequestParam(name = "setting", required = false) List<String> settings,
                                 RedirectAttributes redir) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());

        List<Setting> newSettings = new ArrayList<>();

        if (settings != null) {
            for (String setting: settings) {
                newSettings.add(settingDao.getOne(Long.parseLong(setting)));
            }
        }

        currentUser.setSettings(newSettings);
        userDao.save(currentUser);

        redir.addFlashAttribute("settingsMsg", "Notification setting saved");

        return null;
    }
    
    
    

}
