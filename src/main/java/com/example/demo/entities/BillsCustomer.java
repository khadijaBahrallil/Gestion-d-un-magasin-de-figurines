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

    public enum StatusOrdered {
        PAYEE,
        VALIDEE,
        ANNULEE,
        EN_COURS_D_ENVOI,
        RECUE
    }

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
