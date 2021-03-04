package com.example.demo.controllers;

import com.example.demo.entities.Category;
import com.example.demo.entities.Licence;
import com.example.demo.repos.CategoryRepository;
import com.example.demo.repos.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class LicenceController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LicenceRepository licenceRepository;

    @PostMapping ("/addLicence")
    public String addLicence(@RequestParam String id, @RequestParam String name){
        Category category;
        int new_id;
        try {
            new_id = Integer.parseInt(id);
            category = categoryRepository.findCategoryById(new_id);
            if(name.length() > 255){
                throw new Exception("Nom trop long");
            }
        }catch(Exception e){
            return "licences";
        }
        Licence licence = new Licence();
        licence.setCategory(category);
        licence.setName(name);
        licenceRepository.save(licence);
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
        return categoryList;
    }


}
