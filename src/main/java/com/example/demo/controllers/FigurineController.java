package com.example.demo.controllers;

import com.example.demo.entities.Figurine;
import com.example.demo.repos.FigurineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class FigurineController {

    @Autowired
    private FigurineRepository figurineRepository;

    //Post
    @PostMapping("/addFigurine")
    public String addFigurine(@RequestParam String name) {
        Figurine figurine = new Figurine();
        figurine.setName(name);
        figurineRepository.save(figurine);
        return "indexFigurine";
    }

    @GetMapping("/deleteFigurine")
    public String deleteFigurine(@RequestParam Integer id) {
        figurineRepository.deleteById(id);
        return "Delete complete";
    }


    @GetMapping("/listFigurine")
    public Iterable<Figurine> getFigurine() {
        return figurineRepository.findAll();
    }

    @GetMapping("/findFigurine/{id}")
    public Figurine findFigurineById(@PathVariable Integer id) {
        return figurineRepository.findFigurineById(id);
    }
}
