package com.example.demo.controllers;

import com.example.demo.repos.CategoryRepository;
import com.example.demo.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/indexCategory")
    public String indexCategory(){
        return "indexCategory";
    }


    @GetMapping("/realindex")
    public String realindex(){
        return "realindex";
    }
    //Post
    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return "home";
    }

    @GetMapping("/deleteCategory")
    public String deleteCategory(@RequestParam Integer id) {
        categoryRepository.deleteById(id);
        return "home";
    }


    @GetMapping("/listCategory")
    public Iterable<Category> getCategory() {
        return categoryRepository.findAll();
    }

    @GetMapping("/findCategory/{id}")
    public Category findCategoryById(@PathVariable Integer id) {
        return categoryRepository.findCategoryById(id);
    }
}

