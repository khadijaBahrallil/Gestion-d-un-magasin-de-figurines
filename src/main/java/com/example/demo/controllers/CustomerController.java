package com.example.demo.controllers;

import com.example.demo.entities.Figurine;
import com.example.demo.repos.AddressRepository;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.entities.Customer;
import com.example.demo.repos.FigurineRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entities.Administrator;
import com.example.demo.repos.AdministratorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpSession;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private FigurineRepository figurineRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ActiveUserStore activeUserStore;
    @Autowired
    private AdministratorRepository administratorRepository;

    @GetMapping("/loggedUsers")
    public String getLoggedUsers(Locale locale, Model model) {
        model.addAttribute("customers", activeUserStore.getCustomers());
        return "users";
    }


    @GetMapping("/profile")
    public String profile(Locale locale, Model model) {
        if (findRole().equals("user")) {
            Customer customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0)).get();
            model.addAttribute("lname", customer.getLastName());
            model.addAttribute("fname", customer.getFirstName());
            model.addAttribute("username", customer.getUsername());
            model.addAttribute("address", customer.getAddress());
        }else if (findRole().equals("admin")) {
            Administrator administrator = administratorRepository.findAdministratorByName(activeUserStore.getCustomers().get(0)).get();
            model.addAttribute("lname", administrator.getLastName());
            model.addAttribute("fname", administrator.getFirstName());
            model.addAttribute("username", administrator.getUsername());
            model.addAttribute("address", "Add me");
        }

        return "profile";
    }


    @GetMapping("/index")
    public String index(Model model) {
        List<Figurine> figurinesListSort = figurineRepository.findFigurineLast();
        List<Figurine> figurinesList = new ArrayList<>();
        for(int i = 0; i < figurinesListSort.size(); i++){
            figurinesList.add(figurinesListSort.get(i));
            if(figurinesList.size() == 6){
                i = figurinesListSort.size();
            }
        }
        model.addAttribute("figurineList", figurinesList);
        return "index";
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
        customer.setAddress(addressRepository.findAddressById(1));//change
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


    @GetMapping("/indexlogout")
    public String LogoutUser() {

        System.out.println("logout");
        return "logout";
    }




    @GetMapping("home")
    public String home(Model model) {
        model.addAttribute("role",findRole());
        System.out.println(findRole());
        return "home";
    }

    @GetMapping("/listCustomer")
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/findCustomer/{id}")
    public Optional<Customer> findCustomerByName(@PathVariable String username) {
        return customerRepository.findCustomerByName(username);
    }

    public String findRole(){
        Optional<Customer> customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0));
        if (!customer.isEmpty()){
            return "user";
        } else {
            Optional<Administrator> administrator = administratorRepository.findAdministratorByName(activeUserStore.getCustomers().get(0));
            if (!administrator.isEmpty()){
                return "admin";
            }
        }
        return null;
    }
}
