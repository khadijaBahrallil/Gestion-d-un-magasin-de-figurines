package com.example.demo.controllers;

import com.example.demo.entities.Category;
import com.example.demo.entities.Licence;
import com.example.demo.repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class LicenceController {
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/addLicence")
    public String addLicence(@RequestParam String category, @RequestParam String name){
       // Category category = categoryRepository.findCategoryById();
        System.out.println(category);
        Licence licence = new Licence();
        licence.setName(name);
        return "licences";
    }

    @RequestMapping("/licences")
    public String indexLicence(HttpServletRequest request) throws Exception{
        return "licences";
    }

    /*
    @ModelAttribute("category")
    protected ModelAndView  getAllCategory(){
        ModelAndView mv = new ModelAndView("category");
        List categoryList = categoryRepository.findAll();
        System.out.println(categoryList.size());
        mv.addObject("categoryList", categoryList);
        System.out.println(mv.toString());
        return mv;
    }
    */

    @ModelAttribute("categoryList")
    protected List<Category> getAllCategory(){
        List<Category> categoryList = categoryRepository.findAll();
        System.out.println(categoryList.get(0).getName());
        return categoryList;
    }


}
