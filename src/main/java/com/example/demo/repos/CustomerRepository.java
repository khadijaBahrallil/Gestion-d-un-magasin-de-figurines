package com.example.demo.repos;

import com.example.demo.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query(" select u from Customer u  where u.userName = ?1")
    Optional<Customer> findCustomerByName(String username);
}
