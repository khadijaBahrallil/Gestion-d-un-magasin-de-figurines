package com.example.demo.controllers;

import com.example.demo.entities.Address;
import com.example.demo.entities.Customer;
import com.example.demo.repos.AddressRepository;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
        Customer customer = addressRepository.findCustomerByUserName(userName.get(0));
        try {
            if(customer == null) {
                throw new Exception("Panier inexistant");
            }
        }catch (Exception e){
            return "redirect:/login";
        }
        customer.setAddress(address);

        customerRepository.save(customer);
        addressRepository.save(address);

        return "redirect:/getBasketUser";
    }
    @GetMapping("/address")
    public String addAddress() {return "address";
    }

}
