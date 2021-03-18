package com.example.demo.controllers;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Customer;
import com.example.demo.repos.AdministratorRepository;
import com.example.demo.repos.CustomerRepository;
import com.example.demo.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AdministratorController {

    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private ActiveUserStore activeUserStore;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/addAdministrator")
    public String addAdministrator(Model model) {
        if(findRole(model).equals("admin")){
            return "registeradmin";
        }
        return "redirect:index";
    }

    @PostMapping("/addAdministrator")
    public String addAdministrator(@RequestParam String lastName, @RequestParam String firstName, @RequestParam String userName,
                              @RequestParam String password, @RequestParam Boolean civility) {
        if (lastName.equals("") || firstName.equals("") || userName.equals("") || password.equals("")){
            return "registeradmin";
        }
        Administrator admin = new Administrator();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setUserName(userName);
        passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        admin.setPassword(hashedPassword);
        admin.setCivility(civility);
        administratorRepository.save(admin);
        return "redirect:index";
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
