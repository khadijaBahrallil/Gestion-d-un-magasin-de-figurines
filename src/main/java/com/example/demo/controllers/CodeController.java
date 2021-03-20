package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.repos.AdministratorRepository;
import com.example.demo.repos.CodeRepository;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Controller
public class CodeController {
    @Autowired
    CodeRepository codeRepository;
    @Autowired
    ActiveUserStore activeUserStore;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/listCode")
    public String getCategory(Model model) {
        if(findRole(model).equals("admin")){

            return "listCodeClassic";
        }
        return "redirect:/index";
    }

     @RequestMapping("/code")
     public String code(Model model){
         if(findRole(model).equals("admin")) {
             return "code";
         }
         return "redirect:/index";
     }
    @PostMapping("/addCode")
    public String addCategory(@RequestParam String name, @RequestParam double codeR,
                              @RequestParam java.sql.Date startDate, @RequestParam java.sql.Date endDate,@RequestParam int nb, Model model) {
        if(findRole(model).equals("admin")) {
            PromoCodeClassic code = new PromoCodeClassic();
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            c.add(Calendar.DATE, 1);
            Date new_start = new Date(c.getTimeInMillis());
            c.setTime(endDate);
            c.add(Calendar.DATE, 1);
            Date new_end = new Date(c.getTimeInMillis());
            code.setCode(name);
            code.setReduction(codeR);
            code.setEndDate(new_end);
            code.setStartDate(new_start);
            code.setNbUtilisation(nb);
            codeRepository.save(code);
            return "code";
        }
        return "redirect:/index";
    }

    @PostMapping("/updateCode")
    public String updateCode(@RequestParam String idCode, @RequestParam String name, @RequestParam double codeR,
                             @RequestParam Date startDate, @RequestParam Date endDate,@RequestParam int nb, Model model) {
        if(findRole(model).equals("admin")) {
            int new_id;
            PromoCodeClassic code;
            try {
                new_id = Integer.parseInt(idCode);
                code = codeRepository.findCodeClassicById(new_id);
                if (name.isEmpty() || name.length() > 255) {
                    throw new Exception("Problème avec le nom");
                }
                Calendar c = Calendar.getInstance();
                c.setTime(startDate);
                c.add(Calendar.DATE, 1);
                Date new_start = new Date(c.getTimeInMillis());
                c.setTime(endDate);
                c.add(Calendar.DATE, 1);
                Date new_end = new Date(c.getTimeInMillis());
                code.setCode(name);
                code.setReduction(codeR);
                code.setEndDate(new_end);
                code.setStartDate(new_start);
                code.setNbUtilisation(nb);

            } catch (Exception e) {
                System.out.println("erreur" + e);
                return "redirect:/listCode";
            }
            codeRepository.save(code);
            return "redirect:/listCode";
        }
        return "redirect:/index";
    }
    @PostMapping("/deleteCode")
    public String deleteCategory(@RequestParam String id, Model model) {
        if(findRole(model).equals("admin")) {
            PromoCodeClassic code;
            int new_id;
            try {
                new_id = Integer.parseInt(id);
                code = codeRepository.findCodeClassicById(new_id);
                codeRepository.deleteById(new_id);
            } catch (Exception e) {
                System.out.println("erreur" + e);
                return "redirect:/listCode";
            }
            return "redirect:/listCode";
        }
        return "redirect:/index";
    }

    @RequestMapping("/listCodePerso")
    public String getCategory(@RequestParam String recherche, Model model) {
        if(findRole(model).equals("admin")) {
            List<PromoCodeClassic> codeList = codeRepository.findCodeWithPartOfName(recherche);
            model.addAttribute("codeList", codeList);
            model.addAttribute("recherche", recherche);
            return "listCodeClassic";
        }
        return "redirect:/index";
    }

    @ModelAttribute("codeList")
    protected List<PromoCodeClassic> getAllCode(){
        List<PromoCodeClassic> codeList = codeRepository.findAll();
        return codeList;
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
