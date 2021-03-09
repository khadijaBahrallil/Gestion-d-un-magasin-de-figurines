package com.example.demo.controllers;

import com.example.demo.entities.Category;
import com.example.demo.entities.Licence;
import com.example.demo.entities.Subscription;
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

    @GetMapping("/listLicence")
    public String listLicence() {
        return "listLicence";
    }

    @PostMapping("/updateLicence")
    public String updateCategory(@RequestParam String idLicence, @RequestParam String name, @RequestParam String idCat) {
        int new_id;
        int new_idCat;
        Licence licence;
        Category category;
        try {
            new_id = Integer.parseInt(idLicence);
            new_idCat = Integer.parseInt(idCat);
            licence = licenceRepository.findLicenceById(new_id);
            category = categoryRepository.findCategoryById(new_idCat);
            if(name.isEmpty()){
                throw new Exception("nom vide");
            }
            if(name.equals(licence.getName()) && category.equals(licence.getCategory())){
                throw new Exception("pas de changement");
            }
            List<Licence> listLicence = licenceRepository.findAll();
            for(int i = 0; i < listLicence.size(); i++){
                if(listLicence.get(i).getName().equals(name) && listLicence.get(i).getCategory().equals(category)){
                    throw new Exception("Licence déjà existante");
                }
            }
            licence.setName(name);
            licence.setCategory(category);
        }catch (Exception e){
            System.out.println("erreur" +e);
            return "listLicence";
        }
        licenceRepository.save(licence);
        return "listLicence";
    }

    @PostMapping("/deleteLicence")
    public String deleteLicence(@RequestParam String id) {
        Licence licence;
        int new_id;
        try{
            new_id = Integer.parseInt(id);
            licence = licenceRepository.findLicenceById(new_id);
            licenceRepository.deleteById(new_id);
        }catch (Exception e){
            System.out.println("erreur" +e);
            return "listLicence";
        }
        return "listLicence";
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

    @ModelAttribute("licenceList")
    protected List<Licence> getAllLicence(){
        List<Licence> licenceList = licenceRepository.findAll();
        return licenceList;
    }

    @ModelAttribute("categoryList")
    protected List<Category> getAllCategory(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList;
    }

}
