package com.example.demo.controllers;

import com.example.demo.entities.Category;
import com.example.demo.repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class LicenceController {
    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping("/licences")
    public String addLicence(HttpServletRequest request) throws Exception{
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
        List categoryList = categoryRepository.findAll();
        System.out.println(categoryList.size());
        return categoryList;
    }


}
