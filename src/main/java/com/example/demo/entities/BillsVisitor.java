package com.example.demo.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class BillsVisitor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private Address address;
    private String email;
    private String phoneNumber;
    @ManyToMany
    private Collection<Figurine> figurines;
    private float prix;
    public enum StatusOrdered {
        PAYEE,
        VALIDEE,
        ANNULEE,
        EN_COURS_D_ENVOI,
        RECUE
    }


}