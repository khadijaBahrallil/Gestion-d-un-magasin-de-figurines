package com.example.demo.controllers;

import com.example.demo.entities.Licence;
import com.example.demo.repos.CategoryRepository;
import com.example.demo.entities.Category;
import com.example.demo.repos.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LicenceRepository licenceRepository;

    @GetMapping("/indexCategory")
    public String indexCategory(){
        return "indexCategory";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@RequestParam String idCat, @RequestParam String name) {
        int new_id;
        Category category;
        try {
            new_id = Integer.parseInt(idCat);
            category = categoryRepository.findCategoryById(new_id);
            if(name.isEmpty() || name.length() > 255){
                throw new Exception("Prblème avec le nom");
            }
            if(name.equals(category.getName())){
                throw new Exception("pas de changement");
            }
            List<Category> listCategory = categoryRepository.findAll();
            for(int i = 0; i < listCategory.size(); i++){
                if(listCategory.get(i).getName().equals(name)){
                    throw new Exception("Categorie déjà existante");
                }
            }
            category.setName(name);

        }catch (Exception e){
            System.out.println("erreur" +e);
            return "listCategory";
        }
        categoryRepository.save(category);
        return "listCategory";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String name) {
        Category category = new Category();
        List<Category> categorys = categoryRepository.findAll();
        if(name.equals("") || name.length() > 255){
            return "indexCategory";
        }
        for(int i = 0; i < categorys.size(); i++){
            if(categorys.get(i).getName().equals(name)){
                return "indexCategory";
            }
        }
        category.setName(name);
        categoryRepository.save(category);
        return "indexCategory";
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam String id, Model model) {
        Category category;
        int new_id;
        try{
            new_id = Integer.parseInt(id);
            category = categoryRepository.findCategoryById(new_id);
            List<Licence> licenceList = licenceRepository.findAll();
            for(int i=0; i< licenceList.size(); i++){
                if(licenceList.get(i).getCategory().getId() == new_id){
                    licenceRepository.deleteById(licenceList.get(i).getId());
                }
            }
            categoryRepository.deleteById(new_id);
        }catch (Exception e){
            System.out.println("erreur" +e);
            return "listCategory";
        }
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);
        return "listCategory";
    }

    @ModelAttribute("categoryList")
    protected List<Category> getAllCategory(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList;
    }


    @GetMapping("/listCategory")
    public String getCategory() {
        return "listCategory";
    }

    @RequestMapping("/listCategoryPerso")
    public String getCategory(@RequestParam String recherche, Model model) {
        List<Category> categoryList = categoryRepository.findCategoryWithPartOfName(recherche);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("recherche",recherche);
        return "listCategory";
    }

    @GetMapping("/findCategory/{id}")
    public Category findCategoryById(@PathVariable Integer id) {
        return categoryRepository.findCategoryById(id);
    }
}

