package com.example.demo.repos;

import com.example.demo.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillsFigurinesRepository extends CrudRepository<BillsCustomerFigurines, BillsCustomerFigurinesPK> {

    @Query(" select bc from BillsCustomerFigurines bc  where bc.figurine = ?1 AND bc.billsCustomer = ?2")
    BillsCustomerFigurines findBillsCustomerFigurineseByFigurineAndBills(Figurine figurine, BillsCustomer bills);

    @Query(" select bc from BillsCustomerFigurines bc  where bc.billsCustomer = ?1")
    List<BillsCustomerFigurines> findBasketBillsCustomerFigurineseByBills(BillsCustomer bills);

}
