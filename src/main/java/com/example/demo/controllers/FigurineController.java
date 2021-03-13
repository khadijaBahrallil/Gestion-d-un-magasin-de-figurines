package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.repos.*;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FigurineController {

    @Autowired
    private FigurineRepository figurineRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LicenceRepository licenceRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    ActiveUserStore activeUserStore;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OpinionRepository opinionRepository;

    //Post
    @PostMapping("/addFigurine")
    public String addFigurine(@RequestParam String name) {
        try {
            if (name.length() > 255) {
                throw new Exception("Erreur, nom trop grand");
            }
        }catch (Exception e){
            return "indexFigurine";
        }
        Figurine figurine = new Figurine();
        figurine.setName(name);
        figurineRepository.save(figurine);
        return "indexFigurine";
    }

    @GetMapping("/recherche")
    public String recherche() {

        return "recherche";
    }

    @PostMapping("/recherche")
    public String rechercheCategorie(@RequestParam("recherche") String recherche, Model model) {
        ArrayList<String> figurines = new ArrayList<>();
        for(Figurine figurine : figurineRepository.findAll()){

            figurines.add(figurine.getName());
        }
        model.addAttribute("figurines", figurines);
        return "recherche";
    }


    @GetMapping("/deleteFigurine")
    public String deleteFigurine(@RequestParam Integer id) {
        figurineRepository.deleteById(id);
        return "Delete complete";
    }

    @RequestMapping("/figurines")
    public String figurines(Model model){
        findRole(model);
        return "figurines";
    }

    @GetMapping("/listFigurine")
    public Iterable<Figurine> getFigurine() {
        return figurineRepository.findAll();
    }

     @PostMapping("/findFigurine")
    public String findFigurine(@RequestParam Integer idFigurine, Model model) {
     Figurine figurine = new Figurine();
     List<Opinion> opinions = new ArrayList<>();
     float note = 0;
     try {
         figurine = figurineRepository.findFigurineById(idFigurine);
         opinions = (List<Opinion>) figurine.getOpinions();
         if(opinions.size() > 0) {
             for (int i = 0; i < opinions.size(); i++) {
                 note = note + opinions.get(i).getNote();
             }
             note = note / opinions.size();
         }

     }catch(Exception e){
         System.out.println(e);
         return "figurines";
     }
     model.addAttribute("figurine", figurine);
     model.addAttribute("opinions", opinions);
     model.addAttribute("note", note);
        return "figurineProfile";
    }

    @GetMapping("/findFigurineProfile/{id}")
    public String findFigurineProfileById(@PathVariable Integer id) {
        return "/figurineProfile";
    }

    @RequestMapping("/listFigurinePerso")
    public String findFigurine(@RequestParam String recherche, @RequestParam String category_name, @RequestParam String license_name, Model model) {
        List<Figurine> figurines;
        List<Figurine> figurinesCategory;
        List<Figurine> figurinesLicence;
        if(recherche.equals("")){
            figurines = figurineRepository.findAll();
        }
        else{
            figurines = figurineRepository.findFigurineWithPartOfName(recherche);
        }
        if(category_name.equals("Toutes")){
            figurinesCategory = figurineRepository.findAll();
        }
        else{
            figurinesCategory = figurineRepository.findFigurineWithCategory(category_name);
        }
        if(license_name.equals("Toutes")){
            figurinesLicence = figurineRepository.findAll();
        }
        else{
            figurinesLicence = figurineRepository.findFigurineWithLicence(license_name);
        }
        List<Figurine> allfigurines = figurineRepository.findAll();
        List<Figurine> figurines2Return = new ArrayList<>();
        Figurine figurine;
        for (int i = 0; i < allfigurines.size(); i++){
            figurine = allfigurines.get(i);
            if(figurines.contains(figurine) && figurinesCategory.contains(figurine) && figurinesLicence.contains(figurine)){
                figurines2Return.add(figurine);
            }
        }
        model.addAttribute("figurineList", figurines2Return);
        model.addAttribute("recherche", recherche);
        model.addAttribute("category_name", category_name);
        model.addAttribute("license_name", license_name);
        findRole(model);
        return "/figurines";
    }
    @ModelAttribute("figurineList")
    protected List<Figurine> getAllFigurine(){
        List<Figurine> figurineList = figurineRepository.findAll();
        return figurineList;
    }


    @ModelAttribute("categoryList")
    protected List<Category> getAllCategory(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList;
    }

    @ModelAttribute("licenceList")
    protected List<Licence> getAllLicence(){
        List<Licence> licenceList = licenceRepository.findAll();
        return licenceList;
    }

    @ModelAttribute("pictureList")
    protected List<Picture> getAllPicture(){
        List<Picture> pictureList = pictureRepository.findAll();
        return pictureList;
    }

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
            return "visitor";        }
    }
}
