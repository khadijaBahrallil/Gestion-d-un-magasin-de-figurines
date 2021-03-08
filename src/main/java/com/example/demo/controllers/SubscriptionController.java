package com.example.demo.controllers;

import com.example.demo.entities.Subscription;
import com.example.demo.repos.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;


@Controller
public class SubscriptionController {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

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
    public String deleteSubscription(@RequestParam String id) {
        Subscription subscription;
        int new_id;
        try{
            new_id = Integer.parseInt(id);
            subscription = subscriptionRepository.findSubscriptionById(new_id);
            subscriptionRepository.deleteById(new_id);
        }catch (Exception e){
            System.out.println("erreur" +e);
            return "listSubscription";
        }
        return "listSubscription";
    }

    @GetMapping("/listSubscription")
    public String listSubscription() {
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
