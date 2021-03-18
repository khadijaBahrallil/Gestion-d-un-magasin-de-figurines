package com.example.demo.repos;

import com.example.demo.entities.BillsCustomer;
import com.example.demo.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillsRepository extends CrudRepository<BillsCustomer, Integer> {

    @Query(" select b from BillsCustomer b  where b.customer = ?1")
    List<BillsCustomer> findBillsByCustomerID(Customer customer);

}
