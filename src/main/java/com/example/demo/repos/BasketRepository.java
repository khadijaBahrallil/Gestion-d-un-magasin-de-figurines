package com.example.demo.repos;

import com.example.demo.entities.Basket;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Figurine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<Basket, Integer> {

    @Query(" select u from Basket u  where u.customer = ?1")
    Basket findBasketByCustomerID(Customer customer);

    @Query(" select u from Customer u  where u.id = ?1")
    Customer findCustomerByID(Integer customerID);

    @Query(" select u from Customer u  where u.userName = ?1")
    Customer findCustomerByUserName(String userName);

    @Query(" select u from Figurine u  where u.id = ?1")
    Figurine findFigurineByID(Integer figurineID);

}
