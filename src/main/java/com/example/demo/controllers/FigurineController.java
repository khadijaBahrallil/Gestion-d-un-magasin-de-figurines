package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.repos.*;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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
    public String addFigurine(@RequestParam String name, @RequestParam Integer idLicense, @RequestParam String description, @RequestParam Date startDate,
                              @RequestParam Float price, @RequestParam Integer quantity, @RequestParam StatusFigurine status) {
        try {
            if (name.length() > 255) {
                throw new Exception("Erreur, nom trop grand");
            }
            if (description.length() > 255) {
                throw new Exception("Erreur, description trop grande");
            }
            if (price < 0) {
                throw new Exception("Erreur, prix");
            }
            if (quantity < 0) {
                throw new Exception("Erreur, quantity");
            }
        }catch (Exception e){
            return "redirect:/index";
        }
        Picture picture = pictureRepository.findPictureByName("default");
        Figurine figurine = new Figurine();
        figurine.setName(name);
        figurine.setQuantity(quantity);
        figurine.setStatus(status);
        figurine.setDescription(description);
        figurine.setPrice(price);
        figurine.setPicture(picture);
        figurine.setLicence(licenceRepository.findLicenceById(idLicense));
        figurine.setStartDate(startDate);
        figurineRepository.save(figurine);


        return "redirect:/indexFigurine";
    }

    @RequestMapping("/indexFigurine")
    public String indexFigurine(Model model) {

        if(findRole(model).equals("admin")){
            System.out.println(StatusFigurine.values());
            model.addAttribute("status", StatusFigurine.values());
            return "indexFigurine";
        }
        return "redirect:/index";
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

    /**
     * This method calculates the note average and checks the rights
     * @param idFigurine
     * @param model
     * @return figurines if error else figurineProfile
     */
     @PostMapping("/findFigurine")
     public String findFigurine(@RequestParam Integer idFigurine, Model model) {
         Figurine figurine = new Figurine();
         List<Opinion> opinions = new ArrayList<>();
         Float note = noteFigurine(idFigurine);
         if(note != null){
             figurine = figurineRepository.findFigurineById(idFigurine);
             opinions = (List<Opinion>) figurine.getOpinions();
         }
         else{
             return "redirect:/figurines";
         }
         model.addAttribute("figurine", figurine);
         model.addAttribute("opinions", opinions);
         model.addAttribute("note", note);
         findRole(model);
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

    /**
     * This method calculae the averqge of the note for one figurine
     * @param idFigurine
     * @return Float null if error else note
     */
    public Float noteFigurine(int idFigurine){
        float note = 0;
        try {
            Figurine figurine = figurineRepository.findFigurineById(idFigurine);
            List<Opinion> opinions = (List<Opinion>) figurine.getOpinions();
            if(opinions.size() > 0) {
                for (int i = 0; i < opinions.size(); i++) {
                    note = note + opinions.get(i).getNote();
                }
                note = note / opinions.size();
            }
            return note;
        }catch(Exception e){
            return null;
        }
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
            return "visitor";        }
    }
}
