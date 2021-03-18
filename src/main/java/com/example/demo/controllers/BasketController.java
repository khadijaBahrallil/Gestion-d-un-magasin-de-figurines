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
     * Modifier la quantité dans le panier
     * @param quantity
     * @return
     */
    /*
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
        Figurine figurine = basketRepository.findFigurineByID(idFigurine);
        if(figurine == null)  return "redirect:/figurines";

        Basket basket = basketRepository.findBasketByCustomerID(customer);

        if(!basket.getQuantityFigurineOfbasket().isEmpty()) {
            if (basket.getQuantityFigurineOfbasket().containsKey(figurine.getId()))
                value = basket.getQuantityFigurineOfbasket().get(figurine.getId());
        }
        value += quantity;

        if(value > figurine.getQuantity()){
            return "redirect:/getBasketUser";
        }

        if(value <= 0 )
            basket.getQuantityFigurineOfbasket().remove(idFigurine);
        else
            basket.getQuantityFigurineOfbasket().put(figurine.getId(),value);

        basket.setSubTotal(quantity*figurine.getPrice());
        basketRepository.save(basket);

        return "redirect:/getBasketUser";
    }
*/
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
            if(quantity > figurine.getQuantity()){

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
     * Afficher le panier
     * @param modelMap
     * @return
     */
    /*
    @GetMapping("/getBasketUser")
    public String getBasketUser( ModelMap modelMap) {
        Customer customer = getActiveCustomer();
        try {
            if(customer == null) {
                throw new Exception("Erreur de connexion");
            }
        }catch (Exception e){
            return "redirect:/login";
        }

        Basket basket = basketRepository.findBasketByCustomerID(customer);
        if(basket == null){
            return "redirect:/basket";
        }

        List<Figurine> listfigurine = new ArrayList<>();

        for(HashMap.Entry<Integer, Integer> mapFig : basket.getQuantityFigurineOfbasket().entrySet()){
            Figurine mockfigurine = basketRepository.findFigurineByID(mapFig.getKey());
            mockfigurine.setQuantity(mapFig.getValue());
            listfigurine.add(mockfigurine);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        double solde = basket.getSubTotal();
        System.out.println(solde);
        modelMap.addAttribute("figurines", listfigurine);
        modelMap.addAttribute("solde", df.format(basket.getSubTotal()));


        return "basket";
    }
*/
    /**
     * Effectuer un achat
     * @return
     */
    @GetMapping("/toBuy")
    public String toBuy(){
        Customer customer = getActiveCustomer();
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
    public Customer getActiveCustomer(){
         List<String> userName = activeUserStore.getCustomers();
         if(userName.isEmpty()) { return null; }
         Customer customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0)).get();
        return customer;
    }

    /**
     * Afficher la facture du paiement
     * @param modelMap
     * @return
     */
    @GetMapping("/getValidBuy")
    public String getFigurine(ModelMap modelMap, Model model) {
        if(findRole(model) != "user"){
            return "redirect:/index";
        }
        Customer customer = getActiveCustomer();
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
/*
        for(HashMap.Entry<Integer, Integer> mapFig : basket.getQuantityFigurineOfbasket().entrySet()){
            Figurine mockfigurine = basketRepository.findFigurineByID(mapFig.getKey());
            mockfigurine.setQuantityBasket(-mapFig.getValue());
            listfigurine.add(mockfigurine);
        }
*/
        Date date = new Date(System.currentTimeMillis());
        DateFormat dft = new SimpleDateFormat("dd/MM/yyyy");

        modelMap.addAttribute("Date", dft.format(date));
        DecimalFormat df = new DecimalFormat("0.00");

        modelMap.addAttribute("figurines", listfigurine);
        modelMap.addAttribute("solde", df.format(basket.getSubTotal()));
        modelMap.addAttribute("customer", basket.getCustomer());
        modelMap.addAttribute("date", dft.format(date));

        basketRepository.delete(basket);

        return "validBuy";
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

    @GetMapping("/validBuy")
    public String validby() {
        return "validBuy";
    }

    public void majPrice(Basket basket){
        List<BasketFigurines> basketFigurines = basketFigurinesRepository.findBasketFigurineByBasket(basket);
        Float price = 0f ;
        Float majPrice;
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
}

