package com.example.demo.repos;

import com.example.demo.entities.Basket;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Figurine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<Basket, Integer> {

    @Query(" select b from Basket b  where b.customer = ?1")
    Basket findBasketByCustomerID(Customer customer);

}
