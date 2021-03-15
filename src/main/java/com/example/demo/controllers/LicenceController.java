package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.repos.AdministratorRepository;
import com.example.demo.repos.CategoryRepository;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.repos.LicenceRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class LicenceController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LicenceRepository licenceRepository;
    @Autowired
    ActiveUserStore activeUserStore;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping ("/addLicence")
    public String addLicence(@RequestParam String id, @RequestParam String name, Model model){
        if(findRole(model).equals("admin")) {
            Category category;
            int new_id;
            try {
                new_id = Integer.parseInt(id);
                if (name.length() > 255) {
                    throw new Exception("Nom trop long");
                }
                category = categoryRepository.findCategoryById(new_id);
            } catch (Exception e) {
                return "redirect:/licences";
            }
            Licence licence = new Licence();
            licence.setCategory(category);
            licence.setName(name);
            licenceRepository.save(licence);
            return "redirect:/licences";
        }
        return "redirect:/index";
    }

    @RequestMapping("/licences")
    public String indexLicence(HttpServletRequest request, Model model) throws Exception{
        if(findRole(model).equals("admin")) {
            return "licences";
        }
        return "redirect:/index";
    }

    @GetMapping("/listLicence")
    public String listLicence(Model model) {
        if(findRole(model).equals("admin")) {
            return "listLicence";
        }
        return "redirect:/index";
    }

    @RequestMapping("/listLicencePerso")
    public String listLicence(@RequestParam String recherche, Model model) {
        if(findRole(model).equals("admin")) {
            List<Licence> licenceList = licenceRepository.findLicenceWithPartOfName(recherche);
            model.addAttribute("licenceList", licenceList);
            model.addAttribute("recherche", recherche);
            return "listLicence";
        }
        return "redirect:/index";
    }

    @PostMapping("/updateLicence")
    public String updateCategory(@RequestParam String idLicence, @RequestParam String name, @RequestParam String idCat, Model model) {
        if(findRole(model).equals("admin")) {
            int new_id;
            int new_idCat;
            Licence licence;
            Category category;
            try {
                new_id = Integer.parseInt(idLicence);
                new_idCat = Integer.parseInt(idCat);
                licence = licenceRepository.findLicenceById(new_id);
                category = categoryRepository.findCategoryById(new_idCat);
                if (name.isEmpty()) {
                    throw new Exception("nom vide");
                }
                if (name.equals(licence.getName()) && category.equals(licence.getCategory())) {
                    throw new Exception("pas de changement");
                }
                List<Licence> listLicence = licenceRepository.findAll();
                for (int i = 0; i < listLicence.size(); i++) {
                    if (listLicence.get(i).getName().equals(name) && listLicence.get(i).getCategory().equals(category)) {
                        throw new Exception("Licence déjà existante");
                    }
                }
                licence.setName(name);
                licence.setCategory(category);
            } catch (Exception e) {
                System.out.println("erreur" + e);
                return "redirect:/listLicence";
            }
            licenceRepository.save(licence);
            return "redirect:/listLicence";
        }
        return "redirect:/index";
    }

    @PostMapping("/deleteLicence")
    public String deleteLicence(@RequestParam String id, Model model) {
        if(findRole(model).equals("admin")) {
            Licence licence;
            int new_id;
            try {
                new_id = Integer.parseInt(id);
                licence = licenceRepository.findLicenceById(new_id);
                licenceRepository.deleteById(new_id);
            } catch (Exception e) {
                System.out.println("erreur" + e);
                return "redirect:/listLicence";
            }
            return "redirect:/listLicence";
        }
        return "redirect:/index";
    }

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

    /**
     * This method found and return th role of currently user
     * @param model
     * @return String in function of the currently user
     */
    public String findRole(Model model){
        try {
            Optional<Customer> customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0));
            if (!customer.isEmpty()) {
                model.addAttribute("role", "user");
                return "user";
            } else {
                Optional<Administrator> administrator = administratorRepository.findAdministratorByName(activeUserStore.getCustomers().get(0));
                if (!administrator.isEmpty()) {
                    model.addAttribute("role", "admin");
                    return "admin";
                }
                else{
                    model.addAttribute("role", "visitor");
                    return "visitor";
                }
            }

        }catch (Exception e){
            model.addAttribute("role", "visitor");
            return "visitor";
        }
    }

}
