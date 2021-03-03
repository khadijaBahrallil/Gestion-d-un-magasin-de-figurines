package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class BillsVisitor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private Address address;
    private String email;
    private int phoneNumber;
    public enum StatusOrdered {
        PAYEE,
        VALIDEE,
        ANNULEE,
        EN_COURS_D_ENVOI,
        RECUE
    }


}
