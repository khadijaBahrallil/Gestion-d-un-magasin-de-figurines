package com.example.demo.controllers;

import com.example.demo.entities.Basket;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Figurine;
import com.example.demo.repos.BasketRepository;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.security.ActiveUserStore;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Controller
public class BasketController {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ActiveUserStore activeUserStore;

    /**
     * Ajouter une figurine au panier
      * @param idFigurine : identifiant de la figurine
     * @param quantity : quantité à ajouter
     * @return
     */
    @PostMapping("/addProductInBasket")
    public String addProductInBasket(@RequestParam Integer idFigurine, @RequestParam Integer quantity){
        Integer value = 0;
        Customer customer = getActivecustomer();
        try {
            if(customer == null) {
                throw new Exception("Panier inexistant");
            }
        }catch (Exception e){
            return "redirect:/login";
        }
        Figurine figurine = basketRepository.findFugirineByID(idFigurine);
        if(figurine == null)  return "redirect:/figurines";

        Basket basket = basketRepository.findBasketByCustomerID(customer);
        if(basket == null)  basket = new Basket(customer);

        if(!basket.getQuantityFigurineOfbasket().isEmpty()) {
            if (basket.getQuantityFigurineOfbasket().containsKey(figurine.getId())) {
                value = basket.getQuantityFigurineOfbasket().get(figurine.getId());
            }
        }
        value += quantity;

        try {
            if(value > figurine.getQuantity()) {
                throw new Exception("Quanté choisi superieur au stock");
            }
        }catch (Exception e){
            return "redirect:/figurines";
        }

        basket.getQuantityFigurineOfbasket().put(figurine.getId(),value);
        basket.setSubTotal(figurine.getPrice()*quantity);

        basketRepository.save(basket);

        return "redirect:/getBasketUser";
    };

    /**
     * Modifier la quantité dans le panier
     * @param quantity
     * @return
     */
    public String updateQantityProductInBasket( Integer idFigurine, Integer quantity){
        Integer value = 0;
        double solde = 0;
        Customer customer = getActivecustomer();
        try {
            if(customer == null) {
                throw new Exception("Panier inexistant");
            }
        }catch (Exception e){
            return "redirect:/login";
        }
        Figurine figurine = basketRepository.findFugirineByID(idFigurine);
        if(figurine == null)  return "redirect:/figurines";

        Basket basket = basketRepository.findBasketByCustomerID(customer);

        if(!basket.getQuantityFigurineOfbasket().isEmpty()) {
            if (basket.getQuantityFigurineOfbasket().containsKey(figurine.getId()))
                value = basket.getQuantityFigurineOfbasket().get(figurine.getId());
        }
        value += quantity;

        if(value > figurine.getQuantity()){
            return "redirect:/figurines";
        }

        if(value <= 0 )
            basket.getQuantityFigurineOfbasket().remove(idFigurine);
        else
            basket.getQuantityFigurineOfbasket().put(figurine.getId(),value);

        basket.setSubTotal(quantity*figurine.getPrice());
        basketRepository.save(basket);

        return "redirect:/getBasketUser";
    }

    /**
     * Ajouter la quantité de +1
     * @return
     */
    @PostMapping("/addQantityOfProduct")
    public String addQantityOfProduct(@RequestParam Integer idFigurine){
        return updateQantityProductInBasket(idFigurine,1);
    }

    /**
     * Diminuer la quantité de -1
     * @return
     */
    @PostMapping("/removeQantityOfProduct")
    public String removeQantityOfProduct(@RequestParam Integer idFigurine){
        return updateQantityProductInBasket(idFigurine,-1);
    }

    /**
     * Vider le panier
     * @return
     */
    @GetMapping("/removeBasket")
    public String removeQantityProductInBasket(){
        Customer customer = getActivecustomer();
        try {
            if(customer == null) {
                throw new Exception("Panier inexistant");
            }
        }catch (Exception e){
            return "redirect:/login";
        }
        Basket basket = basketRepository.findBasketByCustomerID(customer);

        try {
            if(basket == null) {
                throw new Exception("Panier inexistant");
            }
        }catch (Exception e){
            return "redirect:/figurines";
        }

        basketRepository.delete(basket);

        return "redirect:/figurines";
    }



    /**
     * Afficher le panier
     * @param modelMap
     * @return
     */
    @GetMapping("/getBasketUser")
    public String getBasketUser( ModelMap modelMap) {
        Customer customer = getActivecustomer();
        try {
            if(customer == null) {
                throw new Exception("Panier inexistant");
            }
        }catch (Exception e){
            return "redirect:/login";
        }

        Basket basket = basketRepository.findBasketByCustomerID(customer);
        if(basket == null) return "redirect:/basket";

        List<Figurine> listfigurine = new ArrayList<>();

        for(HashMap.Entry<Integer, Integer> mapFig : basket.getQuantityFigurineOfbasket().entrySet()){
            Figurine mockfigurine = basketRepository.findFugirineByID(mapFig.getKey());
            mockfigurine.setQuantity(mapFig.getValue());
            listfigurine.add(mockfigurine);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        modelMap.addAttribute("figurines", listfigurine);
        modelMap.addAttribute("solde", df.format(basket.getSubTotal()));


        return "basket";
    }

    /**
     * Effectuer un achat
     * @return
     */
    @GetMapping("/toBuy")
    public String toBuy(){
        Customer customer = getActivecustomer();
        try {
            if(customer == null) {
                throw new Exception("Panier inexistant");
            }
        }catch (Exception e){
            return "redirect:/login";
        }

        Basket basket = basketRepository.findBasketByCustomerID(customer);

        if(basket == null) return "redirect:/getBasketUser";
        if(customer.getAddress() == null) return "redirect:/address";

        return "redirect:/getValidBuy";
    }

    /**
     * Recuperer l'utilisateur
     * @return
     */
    public Customer getActivecustomer(){
         List<String> userName = activeUserStore.getCustomers();
         if(userName.isEmpty()) { return null; }

        return basketRepository.findCustomerByUserName(userName.get(0));
    }

    /**
     * Afficher la facture du paiement
     * @param modelMap
     * @return
     */
    @GetMapping("/getValidBuy")
    public String getFigurine(ModelMap modelMap) {
        Customer customer = getActivecustomer();
        try {
            if(customer == null) {
                throw new Exception("Panier inexistant");
            }
        }catch (Exception e){
            return "redirect:/login";
        }

        Basket basket = basketRepository.findBasketByCustomerID(customer);
        if(basket == null) return "redirect:/basket";

        List<Figurine> listfigurine = new ArrayList<>();

        for(HashMap.Entry<Integer, Integer> mapFig : basket.getQuantityFigurineOfbasket().entrySet()){
            Figurine mockfigurine = basketRepository.findFugirineByID(mapFig.getKey());
            mockfigurine.setQuantity(mapFig.getValue());
            listfigurine.add(mockfigurine);
        }

        //Date date = new Date();
        //DateFormat dft = new SimpleDateFormat("dd/MM/yyyy");
        //modelMap.addAttribute("Date", dft.format(date));
        DecimalFormat df = new DecimalFormat("0.00");
        modelMap.addAttribute("figurines", listfigurine);
        modelMap.addAttribute("solde", df.format(basket.getSubTotal()));
        modelMap.addAttribute("customer", basket.getCustomer());

        basketRepository.delete(basket);

        return "validBuy";
    }


    @GetMapping("/basket")
    public String basket() {
        return "basket";
    }

    @GetMapping("/validBuy")
    public String validby() {
        return "validBuy";
    }

}

