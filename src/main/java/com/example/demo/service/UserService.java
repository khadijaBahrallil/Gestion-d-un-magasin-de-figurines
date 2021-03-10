package com.example.demo.service;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Customer;
import com.example.demo.repos.AdministratorRepository;
import com.example.demo.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * The type User service.
 */
@Service


public class UserService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final AdministratorRepository administratorRepository;

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     */
    @Autowired
    public UserService(CustomerRepository userRepository, AdministratorRepository administratorRepository) {
        this.customerRepository = userRepository;
        this.administratorRepository = administratorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        Optional<Customer> user = customerRepository.findCustomerByName(username);
        if (!user.isEmpty()) {
            Customer customer = customerRepository.findCustomerByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return  customer;
        } else {
            // Not found in user table, so check admin

            Administrator admin = administratorRepository.findAdministratorByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (admin != null) {
                return admin;
            }
        }
        throw new UsernameNotFoundException("User '" + username + "' not found");

/**
 Customer user = customerRepository.findCustomerByName(username)
 .orElseThrow(() -> new UsernameNotFoundException("User not found"));
 return  user;
 **/
    }
}
