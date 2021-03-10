package com.example.demo.controllers;

import com.example.demo.entities.Category;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Subscription;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.repos.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
public class SubscriptionController {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/addSubscription")
    public String addSubscription(@RequestParam String name, @RequestParam float price, @RequestParam String text) {
        Subscription subscription = new Subscription();
        try{
            if(price < 0 ){
                throw new Exception("prix non conforme");
            }
            if(name.length() > 255){
                throw new Exception("+ de 255 caractères rentrés");
            }
            if(text.length() > 255){
                throw new Exception("+ de 255 caractères rentrés");
            }
        }catch (Exception e){
            System.out.println("erreur" +e);
            return "subscription";
        }
        subscription.setName(name);
        subscription.setPrice(price);
        subscription.setText(text);
        subscriptionRepository.save(subscription);
        return "subscription";
    }

    @PostMapping("/deleteSubscription")
    public String deleteSubscription(@RequestParam String id, Model model) {
        Subscription subscription;
        int new_id;
        try{
            new_id = Integer.parseInt(id);
            subscription = subscriptionRepository.findSubscriptionById(new_id);
            List<Customer> customers = customerRepository.findAll();
            for (int i = 0; i < customers.size(); i++ ){
                if(customers.get(i).getSubscription() != null) {
                    if (customers.get(i).getSubscription().equals(subscription)) {
                        customers.get(i).setSubscription(null);
                        customerRepository.save(customers.get(i));
                    }
                }
            }

            subscriptionRepository.deleteById(new_id);
        }catch (Exception e){
            System.out.println("erreur " +e);
            return "listSubscription";
        }
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        model.addAttribute("subscriptionList", subscriptionList);
        return "listSubscription";
    }

    @PostMapping("/updateSubscription")
    public String updateSubscription(@RequestParam String idSubscription, @RequestParam String name, @RequestParam float price, @RequestParam String text) {
        int new_id;
        Subscription subscription;
        try {
            new_id = Integer.parseInt(idSubscription);
            subscription = subscriptionRepository.findSubscriptionById(new_id);
            if(price < 0){
                throw new Exception("Prix non valide");
            }
            if(text.length() > 255 || text.isEmpty()){
                throw new Exception("Description non valide");
            }
            if(name.isEmpty()){
                throw new Exception("Nom vide");
            }
            List<Subscription> listSubscription = subscriptionRepository.findAll();
            for(int i = 0; i < listSubscription.size(); i++){
                if(listSubscription.get(i).getName().equals(name) && listSubscription.get(i).getPrice() == price && listSubscription.get(i).getText().equals(text)){
                    throw new Exception("Catégorie déjà existante");
                }
            }
            subscription.setName(name);
            subscription.setPrice(price);
            subscription.setText(text);

        }catch (Exception e){
            System.out.println("Erreur" +e);
            return "listSubscription";
        }
        subscriptionRepository.save(subscription);
        return "listSubscription";
    }

    @GetMapping("/listSubscription")
    public String listSubscription() {
        return "listSubscription";
    }

    @RequestMapping("/listSubscriptionPerso")
    public String listSubscription(@RequestParam String recherche, Model model) {
        List<Subscription> subscriptionList = subscriptionRepository.findSubscriptionWithPartOfName(recherche);
        model.addAttribute("subscriptionList", subscriptionList);
        return "listSubscription";
    }

    @GetMapping("/subscription")
    public String subscription() {
        return "subscription";
    }

    @ModelAttribute("subscriptionList")
    protected List<Subscription> getAllSubscription(){
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        return subscriptionList;
    }
}
