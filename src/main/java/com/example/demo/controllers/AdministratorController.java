package com.example.demo.controllers;

import com.example.demo.entities.Administrator;
import com.example.demo.repos.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdministratorController {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/addAdministrator")
    public String addAdministrator() {
            return "registeradmin";

    }

    @PostMapping("/addAdministrator")
    public String addAdministrator(@RequestParam String lastName, @RequestParam String firstName, @RequestParam String userName,
                              @RequestParam String password, @RequestParam Boolean civility) {
        if (lastName.equals("") || firstName.equals("") || userName.equals("") || password.equals("")){
            return "register";
        }
        Administrator admin = new Administrator();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setUserName(userName);
        admin.setAssets(0);
        admin.setBonusPoint(0);
        passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        admin.setPassword(hashedPassword);
        admin.setCivility(civility);
        administratorRepository.save(admin);
        return "index";
    }
}
