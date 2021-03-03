package com.example.demo.controllers;

import com.example.demo.repos.CustomerRepository;
import com.example.demo.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }


    @PostMapping("/addCustomer")
    public String addCustomer(@RequestParam String lastName, @RequestParam String firstName, @RequestParam String userName,
                              @RequestParam String password, @RequestParam Boolean civility) {
        if (lastName.equals("") || firstName.equals("") || userName.equals("") || password.equals("")){
            return "register";
        }
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setUserName(userName);
        passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        customer.setPassword(hashedPassword);
        customer.setCivility(civility);
        customerRepository.save(customer);
        return "index";
    }


    @GetMapping("/register")
    public String addUser() {

        return "register";
    }
    @PostMapping("/addUser")
    public String addUser(Customer costumer, BindingResult result, Model model) {
        System.out.println("testAddUser");
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "index";
        }
        costumer.setPassword(passwordEncoder.encode(costumer.getPassword()));
        customerRepository.save(costumer);
        System.out.println("user créé avec succès");
        return "index";
    }
    @GetMapping("login")
    public String LoginUser() {

        return "login";
    }

    @GetMapping("/listCustomer")
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/findCustomer/{id}")
    public Optional<Customer> findCustomerByName(@PathVariable String username) {
        return customerRepository.findCustomerByName(username);
    }
}
