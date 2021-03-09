package com.example.demo.controllers;

import com.example.demo.entities.Category;
import com.example.demo.entities.Figurine;
import com.example.demo.entities.Licence;
import com.example.demo.entities.Picture;
import com.example.demo.repos.CategoryRepository;
import com.example.demo.repos.FigurineRepository;
import com.example.demo.repos.LicenceRepository;
import com.example.demo.repos.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/deleteFigurine")
    public String deleteFigurine(@RequestParam Integer id) {
        figurineRepository.deleteById(id);
        return "Delete complete";
    }


    @RequestMapping("/figurines")
    public String figurines(){
        return "figurines";
    }

    @RequestMapping("/opinion")
    public String opinion(){ return "opinion";}

    @GetMapping("/listFigurine")
    public Iterable<Figurine> getFigurine() {
        return figurineRepository.findAll();
    }

   /* @RequestMapping("/findFigurine/{id}")
    public Figurine findFigurineById(@PathVariable Integer id) {
        return figurineRepository.findFigurineById(id);
    }
    */
 @PostMapping("/findFigurine")
    public String findFigurine(@RequestParam Integer idFigurine, Model model) {
     Figurine figurine;
     try {
         figurine = figurineRepository.findFigurineById(idFigurine);
     }catch(Exception e){
         return "figurines";
     }
        model.addAttribute("figurine", figurine);
        return "figurineProfile";
    }

    @GetMapping("/findFigurineProfile/{id}")
    public String findFigurineProfileById(@PathVariable Integer id) {
        return "/figurineProfile";
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
}
