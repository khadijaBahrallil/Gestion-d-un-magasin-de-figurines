package com.example.demo.repos;

import com.example.demo.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    List<Customer> findAll();
    Customer findCustomerById(Integer id);
    @Query(" select c from Customer c  where c.userName = ?1")
    Optional<Customer> findCustomerByName(String username);
}
