package com.example.demo.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;

@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private HashMap<Integer,Integer> quantityFigurineOfbasket;
    private double subTotal;

    @OneToOne
    private PromoCodeClassic classicCode;
    @OneToOne
    private Customer customer;
    @OneToOne
    private PromoCodePercent percentCode;
    @OneToMany
    private Collection<Figurine> figurines;

    public Basket() {}
    public Basket(Customer customer) {
        this.customer = customer;
        this.quantityFigurineOfbasket = new HashMap<Integer,Integer>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal += subTotal;
    }

    public PromoCodeClassic getClassicCode() {
        return classicCode;
    }

    public void setClassicCode(PromoCodeClassic classicCode) {
        this.classicCode = classicCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PromoCodePercent getPercentCode() {
        return percentCode;
    }

    public void setPercentCode(PromoCodePercent percentCode) {
        this.percentCode = percentCode;
    }

    public Collection<Figurine> getFigurines() {
        return figurines;
    }

    public void setFigurines(Collection<Figurine> figurines) {
        this.figurines = figurines;
    }

    public HashMap<Integer, Integer> getQuantityFigurineOfbasket() {
        return quantityFigurineOfbasket;
    }

    public void setQuantityFigurineOfbasket(HashMap<Integer, Integer> quantityFigurineOfbasket) {
        this.quantityFigurineOfbasket = quantityFigurineOfbasket;
    }
}
