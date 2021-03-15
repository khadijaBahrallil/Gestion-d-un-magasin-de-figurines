package com.example.demo.controllers;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Licence;
import com.example.demo.repos.AdministratorRepository;
import com.example.demo.repos.CategoryRepository;
import com.example.demo.entities.Category;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.repos.LicenceRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

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

    @GetMapping("/indexCategory")
    public String indexCategory(Model model){
        if(findRole(model).equals("admin")){
            return "indexCategory";
        }
        return "redirect:/index";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@RequestParam String idCat, @RequestParam String name, Model model) {
        if(findRole(model).equals("admin")) {
            int new_id;
            Category category;
            try {
                new_id = Integer.parseInt(idCat);
                category = categoryRepository.findCategoryById(new_id);
                if (name.isEmpty() || name.length() > 255) {
                    throw new Exception("Prblème avec le nom");
                }
                if (name.equals(category.getName())) {
                    throw new Exception("pas de changement");
                }
                List<Category> listCategory = categoryRepository.findAll();
                for (int i = 0; i < listCategory.size(); i++) {
                    if (listCategory.get(i).getName().equals(name)) {
                        throw new Exception("Categorie déjà existante");
                    }
                }
                category.setName(name);

            } catch (Exception e) {
                System.out.println("erreur" + e);
                return "listCategory";
            }
            categoryRepository.save(category);
            return "listCategory";
        }
        return "redirect:/index";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String name, Model model) {
        if(findRole(model).equals("admin")) {
            Category category = new Category();
            List<Category> categorys = categoryRepository.findAll();
            if (name.equals("") || name.length() > 255) {
                return "indexCategory";
            }
            for (int i = 0; i < categorys.size(); i++) {
                if (categorys.get(i).getName().equals(name)) {
                    return "indexCategory";
                }
            }
            category.setName(name);
            categoryRepository.save(category);
            return "indexCategory";
        }
        return "redirect:/index";
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam String id, Model model) {
        if(findRole(model).equals("admin")) {
            Category category;
            int new_id;
            try {
                new_id = Integer.parseInt(id);
                category = categoryRepository.findCategoryById(new_id);
                List<Licence> licenceList = licenceRepository.findAll();
                for (int i = 0; i < licenceList.size(); i++) {
                    if (licenceList.get(i).getCategory().getId() == new_id) {
                        licenceRepository.deleteById(licenceList.get(i).getId());
                    }
                }
                categoryRepository.deleteById(new_id);
            } catch (Exception e) {
                System.out.println("erreur" + e);
                return "redirect:/listCategory";
            }
            return "redirect:/listCategory";
        }
        return "redirect:/index";
    }
    /*
    @ModelAttribute("categoryList")
    protected List<Category> getAllCategory(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList;
    }
*/

    @GetMapping("/listCategory")
    public String getCategory(Model model) {
        if(findRole(model).equals("admin")){
            return "listCategory";
        }
        return "redirect:/index";
    }

    @RequestMapping("/listCategoryPerso")
    public String getCategory(@RequestParam String recherche, Model model) {
        if(findRole(model).equals("admin")) {
            List<Category> categoryList = categoryRepository.findCategoryWithPartOfName(recherche);
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("recherche", recherche);
            return "listCategory";
        }
        return "redirect:/index";
    }

    /*
    @GetMapping("/findCategory/{id}")
    public Category findCategoryById(@PathVariable Integer id) {
        return categoryRepository.findCategoryById(id);
    }
*/

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

