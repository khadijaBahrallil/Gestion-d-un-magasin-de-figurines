package com.example.demo.controllers;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Figurine;
import com.example.demo.entities.Opinion;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.repos.FigurineRepository;
import com.example.demo.repos.OpinionRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OpinionController {

    @Autowired
    private OpinionRepository opinionRepository;
    @Autowired
    private FigurineRepository figurineRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    ActiveUserStore activeUserStore;


    @PostMapping("/opinion")
    public String opinion(@RequestParam int idFigurine, Model model) {
        Figurine figurine = new Figurine();
        Customer customer;
        Opinion opinion;
        List<Opinion> opinions;
        try{
            customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0)).get();
            figurine = figurineRepository.findFigurineById(idFigurine);
            opinions = (List<Opinion>) figurine.getOpinions();
            for(int i = 0; i < opinions.size(); i++){
                if(opinions.get(i).getCustomer().equals(customer)){
                    model.addAttribute("figurine", figurine);
                    model.addAttribute("opinion", opinions.get(i));
                    return "indexOpinion";
                }
            }
            model.addAttribute("figurine", figurine);
        }catch (Exception e){
            System.out.println("erreur " + e);
        }
        return "opinion";
    }
    @PostMapping("/updateOpinion")
    public String updateOpinion(@RequestParam int idOpinion,@RequestParam int note, @RequestParam String text, Model model) {
        Opinion opinion = new Opinion();
        Figurine figurine = new Figurine();
        float noteTotal = 0;
        Customer customer;
        List<Opinion> opinions = new ArrayList<>();
        try{
            if(note < 0 || note > 5){
                throw new Exception("note non conforme");
            }
            if(text.length() > 255){
                throw new Exception("+ de 255 caractères rentrés");
            }
            opinion = opinionRepository.findOpinionById(idOpinion);
            figurine = figurineRepository.findFigurineById(opinion.getFigurine().getId());
            customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0)).get();
        }catch (Exception e){
            System.out.println("erreur" +e);
            model.addAttribute("figurineList", figurineRepository.findAll());
            return "figurines";
        }
        Date date = new Date(System.currentTimeMillis());
        opinion.setNote(note);
        opinion.setText(text);
        opinion.setDate(date);
        opinionRepository.save(opinion);
        try{
            opinions = (List<Opinion>) figurine.getOpinions();
            if(opinions.size() > 0) {
                for (int i = 0; i < opinions.size(); i++) {
                    noteTotal = noteTotal + opinions.get(i).getNote();
                }
                noteTotal = noteTotal / opinions.size();
            }
        }catch (Exception e){
            return "figurines";
        }
        model.addAttribute("figurine", figurine);
        model.addAttribute("opinions", opinions);
        model.addAttribute("note", noteTotal);
        return "figurineProfile";
    }

    @PostMapping("/addOpinion")
    public String addOpinion(@RequestParam int note, @RequestParam String text, @RequestParam int idFigurine, Model model) {
        Opinion opinion = new Opinion();
        Figurine figurine = new Figurine();
        List<Opinion> opinions = new ArrayList<>();
        Customer customer;
        float noteTotal = 0;
        model.addAttribute("idFigurine", idFigurine);
        try{
            if(note < 0 || note > 5){
                throw new Exception("note non conforme");
            }
            if(text.length() > 255){
                throw new Exception("+ de 255 caractères rentrés");
            }
            figurine = figurineRepository.findFigurineById(idFigurine);
            opinion.setFigurine(figurine);
            customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0)).get();
            System.out.println(customer.getLastName());
        }catch (Exception e){
            System.out.println("erreur" +e);
            model.addAttribute("figurineList", figurineRepository.findAll());
            return "figurines";
        }
        opinion.setCustomer(customer);
        opinion.setNote(note);
        opinion.setText(text);
        Date date = new Date(System.currentTimeMillis());
        opinion.setDate(date);
        opinionRepository.save(opinion);
        figurine.setOpinion(opinion);
        figurineRepository.save(figurine);
        try{
            opinions = (List<Opinion>) figurine.getOpinions();
            if(opinions.size() > 0) {
                for (int i = 0; i < opinions.size(); i++) {
                    noteTotal = noteTotal + opinions.get(i).getNote();
                }
                noteTotal = noteTotal / opinions.size();
            }
        }catch (Exception e){
            return "figurines";
        }
        model.addAttribute("figurine", figurine);
        model.addAttribute("opinions", opinions);
        model.addAttribute("note", noteTotal);
        return "figurineProfile";
    }
}
