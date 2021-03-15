package com.example.demo.controllers;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Category;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Subscription;
import com.example.demo.repos.AdministratorRepository;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.repos.SubscriptionRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@Controller
public class SubscriptionController {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    ActiveUserStore activeUserStore;
    @Autowired
    private AdministratorRepository administratorRepository;

    @PostMapping("/addSubscription")
    public String addSubscription(@RequestParam String name, @RequestParam float price, @RequestParam String text, Model model) {
        if(findRole(model).equals("admin")) {
            Subscription subscription = new Subscription();
            try {
                if (price < 0) {
                    throw new Exception("prix non conforme");
                }
                if (name.length() > 255) {
                    throw new Exception("+ de 255 caractères rentrés");
                }
                if (text.length() > 255) {
                    throw new Exception("+ de 255 caractères rentrés");
                }
            } catch (Exception e) {
                System.out.println("erreur" + e);
                return "subscription";
            }
            subscription.setName(name);
            subscription.setPrice(price);
            subscription.setText(text);
            subscriptionRepository.save(subscription);
            return "subscription";
        }
        return "redirect:/index";
    }

    @PostMapping("/deleteSubscription")
    public String deleteSubscription(@RequestParam String id, Model model) {
        if(findRole(model).equals("admin")) {
            Subscription subscription;
            int new_id;
            try {
                new_id = Integer.parseInt(id);
                subscription = subscriptionRepository.findSubscriptionById(new_id);
                List<Customer> customers = customerRepository.findAll();
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getSubscription() != null) {
                        if (customers.get(i).getSubscription().equals(subscription)) {
                            customers.get(i).setSubscription(null);
                            customerRepository.save(customers.get(i));
                        }
                    }
                }

                subscriptionRepository.deleteById(new_id);
            } catch (Exception e) {
                System.out.println("erreur " + e);
                return "redirect:/listSubscription";
            }
            return "redirect:/listSubscription";
        }
        return "redirect:/index";
    }

    @PostMapping("/updateSubscription")
    public String updateSubscription(@RequestParam String idSubscription, @RequestParam String name, @RequestParam float price, @RequestParam String text, Model model) {
        if(findRole(model).equals("admin")) {
            int new_id;
            Subscription subscription;
            try {
                new_id = Integer.parseInt(idSubscription);
                subscription = subscriptionRepository.findSubscriptionById(new_id);
                if (price < 0) {
                    throw new Exception("Prix non valide");
                }
                if (text.length() > 255 || text.isEmpty()) {
                    throw new Exception("Description non valide");
                }
                if (name.isEmpty()) {
                    throw new Exception("Nom vide");
                }
                List<Subscription> listSubscription = subscriptionRepository.findAll();
                for (int i = 0; i < listSubscription.size(); i++) {
                    if (listSubscription.get(i).getName().equals(name) && listSubscription.get(i).getPrice() == price && listSubscription.get(i).getText().equals(text)) {
                        throw new Exception("Catégorie déjà existante");
                    }
                }
                subscription.setName(name);
                subscription.setPrice(price);
                subscription.setText(text);

            } catch (Exception e) {
                System.out.println("Erreur" + e);
                return "redirect:/listSubscription";
            }
            subscriptionRepository.save(subscription);
            return "redirect:/listSubscription";
        }
        return "redirect:/index";
    }

    @GetMapping("/listSubscription")
    public String listSubscription(Model model) {
        if(findRole(model).equals("admin")) {
            return "listSubscription";
        }
        return "redirect:/index";
    }

    @RequestMapping("/listSubscriptionPerso")
    public String listSubscription(@RequestParam String recherche, Model model) {
        if(findRole(model).equals("admin")) {
            List<Subscription> subscriptionList = subscriptionRepository.findSubscriptionWithPartOfName(recherche);
            model.addAttribute("subscriptionList", subscriptionList);
            return "listSubscription";
        }
        return "redirect:/index";
    }

    @GetMapping("/subscription")
    public String subscription(Model model) {
        if(findRole(model).equals("admin")) {
            return "subscription";
        }
        return "redirect:/index";
    }

    @ModelAttribute("subscriptionList")
    protected List<Subscription> getAllSubscription(){
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        return subscriptionList;
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
