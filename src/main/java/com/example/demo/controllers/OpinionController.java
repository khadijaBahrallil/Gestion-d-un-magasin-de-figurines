package com.example.demo.controllers;

import com.example.demo.entities.Opinion;
import com.example.demo.repos.OpinionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
public class OpinionController {

    @Autowired
    private OpinionRepository opinionRepository;

    @GetMapping("/addOpinion")
    public String addOpinion(@RequestParam int note, @RequestParam String text) {
        Opinion opinion = new Opinion();
        try{
            if(note < 0 || note > 5){
                throw new Exception("note non conforme");
            }
            if(text.length() > 255){
                throw new Exception("+ de 255 caractères rentrés");
            }
        }catch (Exception e){
            System.out.println("erreur" +e);
            return "opinion";
        }
        opinion.setNote(note);
        opinion.setText(text);
        Date date = new Date(System.currentTimeMillis());
        opinion.setDate(date);
        opinionRepository.save(opinion);
        return "opinion";
    }
}
