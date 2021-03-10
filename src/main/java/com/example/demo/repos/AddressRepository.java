package com.example.demo.repos;

import com.example.demo.entities.Address;
import com.example.demo.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address,Integer> {
    @Query(" select u from Customer u  where u.userName = ?1")
    Customer findCustomerByUserName(String userName);
}
