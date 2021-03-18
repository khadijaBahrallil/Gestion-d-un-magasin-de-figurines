package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.repos.*;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BillController {

    @Autowired
    private ActiveUserStore activeUserStore;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private BasketFigurinesRepository basketFigurinesRepository;
    @Autowired
    private BillsRepository billsRepository;
    @Autowired
    private BillsFigurinesRepository billsFigurinesRepository;

    @RequestMapping("/bill")
    public String bill(Model model){
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
        if(basket == null){
            return "redirect:/figurines";
        }
        Double solde = basket.getSubTotal();
        if(solde == 0 ){
            return "redirect:/figurines";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

        List<BasketFigurines> basketFigurines = basketFigurinesRepository.findBasketFigurineByBasket(basket);
        model.addAttribute("date", new SimpleDateFormat("dd-MM-yyyy").format(date));
        model.addAttribute("customer", customer);
        model.addAttribute("basketFigurines", basketFigurines);
        model.addAttribute("solde", df.format(solde));
        findRole(model);
        return "bill";
    }

    /**
     * Afficher la facture du paiement
     * @param model
     * @return
     */
    @GetMapping("/getValidBuy")
    public String getFacture(Model model) {
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
        if (customer.getAddressDelivery() == null){
            return "redirect:/bill";
        }
        Basket basket = basketRepository.findBasketByCustomerID(customer);
        if(basket == null){
            return "redirect:/figurines";
        }
        Double solde = basket.getSubTotal();
        if(solde == 0 ){
            return "redirect:/figurines";
        }
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        BillsCustomer billsCustomer = new BillsCustomer();
        billsCustomer.setCustomer(customer);
        billsCustomer.setPromoCodeClassic(basket.getClassicCode());
        billsCustomer.setPromoCodePercent(basket.getPromoCodePercent());
        billsCustomer.setSubTotal(basket.getSubTotal());
        billsCustomer.setDate(date);
        billsCustomer.setStatus(StatusOrdered.PAYEE);
        billsRepository.save(billsCustomer);


        DecimalFormat df = new DecimalFormat("0.00");
        BillsCustomerFigurines billsCustomerFigurines;
        List<BillsCustomerFigurines>  billsCustomerFigurinesList = new ArrayList<>();
        List<BasketFigurines> basketFigurines = basketFigurinesRepository.findBasketFigurineByBasket(basket);
        for (int i = 0; i < basketFigurines.size(); i++){
            billsCustomerFigurines = new BillsCustomerFigurines();
            billsCustomerFigurines.setBillsCustomer(billsCustomer);
            billsCustomerFigurines.setFigurine(basketFigurines.get(i).getFigurine());
            billsCustomerFigurines.setQuantite(basketFigurines.get(i).getQuantite());
            billsCustomerFigurines.setUnitPrice(basketFigurines.get(i).getUnitPrice());
            billsCustomerFigurinesList.add(billsCustomerFigurines);
            billsFigurinesRepository.save(billsCustomerFigurines);
            basketFigurinesRepository.delete(basketFigurines.get(i));
        }
        model.addAttribute("date", new SimpleDateFormat("dd-MM-yyyy").format(date));
        model.addAttribute("customer", customer);
        model.addAttribute("basketFigurines", billsCustomerFigurinesList);
        model.addAttribute("solde", df.format(solde));
        findRole(model);
        basketRepository.delete(basket);
        //basketRepository.delete(basket);

        return "validBuy";
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
