package com.example.demo.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(BillsCustomerFigurinesPK.class)
public class BillsCustomerFigurines implements Serializable {
    @Id
    @ManyToOne
    private BillsCustomer billsCustomer;

    @Id
    @ManyToOne
    private Figurine figurine;

    private Integer quantite;
    private Float unitPrice;

}
