package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class BillsCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private Customer customer;
    public enum StatusOrdered {
        PAYEE,
        VALIDEE,
        ANNULEE,
        EN_COURS_D_ENVOI,
        RECUE
    }


}
