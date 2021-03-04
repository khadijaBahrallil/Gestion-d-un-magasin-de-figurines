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

    //Post
    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return "Added new category to repo!";
    }

    @GetMapping("/deleteCategory")
    public String deleteCategory(@RequestParam Integer id) {
        categoryRepository.deleteById(id);
        return "Delete complete";
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

