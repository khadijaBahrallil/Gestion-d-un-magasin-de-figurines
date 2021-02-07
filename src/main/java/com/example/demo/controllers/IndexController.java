package com.example.demo.controllers;

import com.example.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.repositories.UserRepo;

@Controller
public class IndexController {
    private final UserRepo userRepo;
    @Autowired
    IndexController(UserRepo userRepo) {this.userRepo = userRepo;}

    @GetMapping("/")
    public String index() {return "index";}

    @GetMapping("/add")
    public String addUser() {return "add";}

    @PostMapping("/add")
    public String addUser(User user, BindingResult result, Model model) {
        if(result.hasErrors()) {return "index";}
        userRepo.save(user);
        System.out.println("user créé avec succès");
        return "index";
    }
}

