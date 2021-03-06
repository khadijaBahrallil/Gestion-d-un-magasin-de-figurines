package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.repos.*;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
    @Autowired
    private BillsRepository billsRepository;


    @GetMapping("/loggedUsers")
    public String getLoggedUsers(Locale locale, Model model) {
        model.addAttribute("customers", activeUserStore.getCustomers());
        return "users";
    }


    @GetMapping("/profile")
    public String profile(Model model) {
        if(findInfoRole(model).equals("visitor")){
            return "redirect:/index";
        }
        if(findInfoRole(model).equals("user")) {
            Customer customer = getActiveCustomer();
            List<BillsCustomer> billsCustomer =  billsRepository.findBillsByCustomerID(customer);
            model.addAttribute("billsCustomer", billsCustomer);
        }
        return "profile";
    }

    @RequestMapping("/")
    public String indexa(Model model){
        return "redirect:/index";
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
        findRole(model);
        return "index";
    }



    @PostMapping("/addCustomer")
    public String addCustomer(@RequestParam String lastName, @RequestParam String firstName, @RequestParam String userName,
                              @RequestParam String password, @RequestParam Boolean civility, Model model) {
        if (lastName.equals("") || firstName.equals("") || userName.equals("") || password.equals("")){
            return "redirect:/register";
        }
        List<Customer>customers = customerRepository.findAll();
        for(int i=0; i< customers.size(); i++){
            if(customers.get(i).getUsername().equals(userName)){
                System.out.println("mail existant");
                return "redirect:/register";
            }
        }
        /* Verif
        if(password.length() < 8){
            return "redirect:/register";
        }
        */
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
        findRole(model);
        return "redirect:/index";
    }


    @GetMapping("/register")
    public String addUser(Model model) {
        if(findRole(model) != "visitor"){
            return "redirect:/index";
        }
        return "register";
    }


    @PostMapping("/addUser")
    public String addUser(Customer costumer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "redirect:/index";
        }
        costumer.setPassword(passwordEncoder.encode(costumer.getPassword()));
        customerRepository.save(costumer);
        System.out.println("user créé avec succès");
        return "redirect:/index";
    }

    @GetMapping("login")
    public String LoginUser(Model model) {
        if(findInfoRole(model) != ("visitor")){
            return "redirect:/index";
        }
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "redirect:/index";
    }

    @GetMapping("/listCustomer")
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
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

    public String findInfoRole(Model model){
        try {
            Optional<Customer> custo = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0));
            if (!custo.isEmpty()) {
                Customer customer = customerRepository.findCustomerByName(activeUserStore.getCustomers().get(0)).get();
                model.addAttribute("lname", customer.getLastName());
                model.addAttribute("fname", customer.getFirstName());
                model.addAttribute("username", customer.getUsername());
                model.addAttribute("address", customer.getAddress());
                model.addAttribute("addressDelevery", customer.getAddressDelivery());
                model.addAttribute("role", "user");
                return "user";
            } else {
                Administrator administrator = administratorRepository.findAdministratorByName(activeUserStore.getCustomers().get(0)).get();
                model.addAttribute("lname", administrator.getLastName());
                model.addAttribute("fname", administrator.getFirstName());
                model.addAttribute("username", administrator.getUsername());
                model.addAttribute("role", "admin");
                return "admin";
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
