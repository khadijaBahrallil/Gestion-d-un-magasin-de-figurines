package com.example.demo.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class BillsCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private StatusOrdered status;

    @OneToOne
    private PromoCodeClassic promoCodeClassic;
    @OneToOne
    private PromoCodePercent promoCodePercent;

    private double subTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
