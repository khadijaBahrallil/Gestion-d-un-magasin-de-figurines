package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.repos.*;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class BasketController {

    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private BasketFigurinesRepository basketFigurinesRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FigurineRepository figurineRepository;

    @Autowired
    private ActiveUserStore activeUserStore;
    @Autowired
    private AdministratorRepository administratorRepository;

    /**
     * Ajouter une figurine au panier
      * @param idFigurine : identifiant de la figurine
     * @param quantity : quantité à ajouter
     * @return
     */
    @PostMapping("/addProductInBasket")
    public String addProductInBasket(@RequestParam Integer idFigurine, @RequestParam Integer quantity){
        Integer value = 0;
        Customer customer = getActiveCustomer();
        try {
            if(customer == null) {
                throw new Exception("Non connecté");
            }
        }catch (Exception e){
            return "redirect:/login";
        }
        Figurine figurine = figurineRepository.findFigurineById(idFigurine);
        if(figurine == null) {
            return "redirect:/figurines";
        }
        if(quantity > figurine.getQuantity()){
            //"Quantité choisie supérieure au stock"
            return "redirect:/figurines";
        }
        Basket basket = basketRepository.findBasketByCustomerID(customer);
        if(basket == null){
            basket = new Basket();
            basket.setCustomer(customer);
            basket.setSubTotal(0);
            basketRepository.save(basket);
        }
        BasketFigurines basketFigurines = basketFigurinesRepository.findBasketFigurineByFigurineAndBasket(figurine, basket);
        if(basketFigurines == null){
            basketFigurines = new BasketFigurines();
            basketFigurines.setBasket(basket);
            basketFigurines.setFigurine(figurine);
            basketFigurines.setQuantite(quantity);
            basketFigurines.setUnitPrice(figurine.getPrice());
        }
        else{
            if(basketFigurines.getQuantite() + quantity <= figurine.getQuantity()){
                basketFigurines.setQuantite(basketFigurines.getQuantite() + quantity);

            }
            basketFigurines.setUnitPrice(figurine.getPrice());
        }
        int figurineQuantity = figurine.getQuantity();
        figurine.setQuantity(figurineQuantity - quantity);
        figurineRepository.save(figurine);
        basketFigurinesRepository.save(basketFigurines);
        return "redirect:/basket";
    };

    /**
     * Ajouter la quantité de +1
     * @return
     */

    @PostMapping("/addQuantityOfProduct")
    public String addQuantityOfProduct(@RequestParam Integer idFigurine){
        Customer customer = getActiveCustomer();
        try {
            Basket basket = basketRepository.findBasketByCustomerID(customer);
            Figurine figurine = figurineRepository.findFigurineById(idFigurine);
            BasketFigurines basketFigurines = basketFigurinesRepository.findBasketFigurineByFigurineAndBasket(figurine, basket);
            int quantity = basketFigurines.getQuantite() + 1;

            if(figurine.getQuantity() <= 0 ){

            }
            else{
                int figurineQuantity = figurine.getQuantity();
                figurine.setQuantity(figurineQuantity - 1);
                figurineRepository.save(figurine);
                basketFigurines.setQuantite(quantity);
                basketFigurines.setUnitPrice(figurine.getPrice());
                basketFigurinesRepository.save(basketFigurines);
            }
        }catch (Exception e){
            return "redirect:/basket";
        }

        return "redirect:/basket";
    }

    /**
     * Diminuer la quantité de -1
     * @return
     */
    @PostMapping("/removeQuantityOfProduct")
    public String removeQuantityOfProduct(@RequestParam Integer idFigurine){
        Customer customer = getActiveCustomer();
        try {
            Basket basket = basketRepository.findBasketByCustomerID(customer);
            Figurine figurine = figurineRepository.findFigurineById(idFigurine);
            BasketFigurines basketFigurines = basketFigurinesRepository.findBasketFigurineByFigurineAndBasket(figurine, basket);
            int quantity = basketFigurines.getQuantite() - 1;
            int figurineQuantity;
            if(quantity <= 0){
                figurineQuantity = figurine.getQuantity();
                figurine.setQuantity(figurineQuantity + 1);
                figurineRepository.save(figurine);
                basketFigurinesRepository.delete(basketFigurines);
            }
            else{
                figurineQuantity = figurine.getQuantity();
                figurine.setQuantity(figurineQuantity + 1);
                figurineRepository.save(figurine);
                basketFigurines.setQuantite(quantity);
                basketFigurines.setUnitPrice(figurine.getPrice());
                basketFigurinesRepository.save(basketFigurines);
            }
            List<BasketFigurines> basketFigurines2 = basketFigurinesRepository.findBasketFigurineByBasket(basket);
            if(basketFigurines2.size() == 0){
                basketRepository.delete(basket);
            }
        }catch (Exception e){
            return "redirect:/basket";
        }

        return "redirect:/basket";
        //return updateQantityProductInBasket(idFigurine,-1);
    }
    /**
     * Vider le panier
     * @return
     */
    @GetMapping("/removeBasket")
    public String removeQuantityProductInBasket(){
        Customer customer = getActiveCustomer();
        try {
            if(customer == null) {
                throw new Exception("Impossible de récupérer l'utilisateur");
            }
        }catch (Exception e){
            return "redirect:/login";
        }

        try {
            Basket basket = basketRepository.findBasketByCustomerID(customer);
            List<BasketFigurines> basketFigurines = basketFigurinesRepository.findBasketFigurineByBasket(basket);
            for(int i = 0; i < basketFigurines.size(); i++){
                int figurineQuantity = basketFigurines.get(i).getFigurine().getQuantity();
                basketFigurines.get(i).getFigurine().setQuantity(figurineQuantity + basketFigurines.get(i).getQuantite());
                figurineRepository.save(basketFigurines.get(i).getFigurine());
                basketFigurinesRepository.delete(basketFigurines.get(i));
            }
            basketRepository.delete(basket);
        }catch (Exception e){
            return "redirect:/figurines";
        }

        return "redirect:/basket";
    }

    /**
     * Effectuer un achat
     * @return
     */
    @GetMapping("/toBuy")
    public String toBuy(){
        Customer customer = getActiveCustomer();
        try {
            if(customer == null) {
                throw new Exception("Impossible de récupérer l'utilisateur");
            }
        }catch (Exception e){
            return "redirect:/login";
        }

        Basket basket = basketRepository.findBasketByCustomerID(customer);

        if(basket == null) return "redirect:/figurines";
        //if(customer.getAddress() == null) return "redirect:/address";

        return "redirect:/getValidBuy";
    }

    @GetMapping("/basket")
    public String basket(Model model) {
        Customer customer = getActiveCustomer();
        Basket basket = basketRepository.findBasketByCustomerID(customer);
        DecimalFormat df = new DecimalFormat("0.00");
        double solde;
        try {
            majPrice(basket);
            solde = basket.getSubTotal();
            if(solde == 0 ){
                model.addAttribute("vide", true);
            }
            else {
                model.addAttribute("vide", false);
            }
        }catch (Exception e){
            solde = 0;
            model.addAttribute("vide", true);
        }
        List<BasketFigurines> basketFigurines = basketFigurinesRepository.findBasketFigurineByBasket(basket);
        model.addAttribute("basketFigurines", basketFigurines);
        model.addAttribute("solde", df.format(solde));
        findRole(model);
        return "basket";
    }

    public void majPrice(Basket basket){
        List<BasketFigurines> basketFigurines = basketFigurinesRepository.findBasketFigurineByBasket(basket);
        double price = 0 ;
        double majPrice;
        for(int i = 0; i < basketFigurines.size(); i++){
            majPrice = basketFigurines.get(i).getFigurine().getPrice();
            basketFigurines.get(i).setUnitPrice(majPrice);
            basketFigurinesRepository.save(basketFigurines.get(i));
            price = price + (majPrice * basketFigurines.get(i).getQuantite());

        }
        basket.setSubTotal(price);
        basketRepository.save(basket);
        System.out.println("sauvegarde basket "+ price);
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
                    return "admin";                }
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

    /**
     * Recuperer l'utilisateur
     * @return
     */
    public Customer getActiveCustomer(){
        List<String> userName = activeUserStore.getCustomers();
        if(userName.isEmpty()) { return null; }
        Customer customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0)).get();
        return customer;
    }

}

