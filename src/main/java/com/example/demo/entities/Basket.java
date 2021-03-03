package com.example.demo.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private PromoCodeClassic classicCode;
    @OneToOne
    private Visitor visitor;
    @OneToOne
    private Customer customer;
    @OneToOne
    private PromoCodePercent percentCode;
    @OneToMany
    private Collection<Figurine> figurines;
}
