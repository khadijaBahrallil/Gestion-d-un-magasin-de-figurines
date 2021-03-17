package com.example.demo.controllers;

import com.example.demo.entities.Address;
import com.example.demo.entities.Administrator;
import com.example.demo.entities.Customer;
import com.example.demo.repos.AddressRepository;
import com.example.demo.repos.AdministratorRepository;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AddressController {

    private String postalCode;
    private String city;
    private String street;
    private String streetnumber;
    private String country;

    @Autowired
    private ActiveUserStore activeUserStore;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @PostMapping("/addAddress")
    public String addAddress(@RequestParam String streetnumber, @RequestParam String street, @RequestParam String postalCode,
                              @RequestParam String city, @RequestParam String country) {
        if (streetnumber.equals("") || street.equals("") || postalCode.equals("") || city.equals("") || country.equals("")){
            return "redirect:/address";
        }
        Address address = new Address();
        address.setStreetnumber(streetnumber);
        address.setStreet(street);
        address.setPostalCode(postalCode);
        address.setCity(city);
        address.setCountry(country);

        List<String> userName = activeUserStore.getCustomers();
        if(userName.isEmpty())  return "redirect:/login";
        Optional<Customer> custo = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0));
        try {
            if(custo.isEmpty()) {
                throw new Exception("Panier inexistant");
            }
            Customer customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0)).get();
            customer.setAddress(address);
            customerRepository.save(customer);
            addressRepository.save(address);
        }catch (Exception e){
            return "redirect:/login";
        }

        return "redirect:/getBasketUser";
    }

    @GetMapping("/address")
    public String addAddress(Model model) {
        findRole(model);
        return "address";
    }

    public void findRole(Model model){
        try {
            Optional<Customer> customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0));
            if (!customer.isEmpty()) {
                model.addAttribute("role", "user");
                System.out.println("user");
            } else {
                Optional<Administrator> administrator = administratorRepository.findAdministratorByName(activeUserStore.getCustomers().get(0));
                if (!administrator.isEmpty()) {
                    model.addAttribute("role", "admin");
                    System.out.println("admin");
                }
                else{
                    model.addAttribute("role", "visitor");
                    System.out.println("visitor");
                }
            }

        }catch (Exception e){
            model.addAttribute("role", "visitor");
            System.out.println("visitor");
        }
    }
}
