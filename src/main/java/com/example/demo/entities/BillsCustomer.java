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

    public Collection<Figurine> getFigurines() {
        return figurines;
    }

    public void setFigurines(Collection<Figurine> figurines) {
        this.figurines = figurines;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
}
