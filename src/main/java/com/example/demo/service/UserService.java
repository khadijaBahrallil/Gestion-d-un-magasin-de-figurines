package com.example.demo.service;

import com.example.demo.entities.Customer;
import com.example.demo.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * The type User service.
 */
@Service


public class UserService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     */
    @Autowired
    public UserService(CustomerRepository userRepository) {
        this.customerRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);

        Customer user = customerRepository.findCustomerByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return  user;
    }
}
