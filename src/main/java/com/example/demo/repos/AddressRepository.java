package com.example.demo.repos;


import com.example.demo.entities.Address;
import com.example.demo.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Integer> {

    List<Address> findAll();
    Address findAddressById(Integer id);

    @Query(" select u from Customer u  where u.userName = ?1")
    Customer findCustomerByUserName(String userName);
}
