package com.example.demo.repos;


import com.example.demo.entities.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Integer> {

    List<Address> findAll();
    Address findAddressById(Integer id);
}
