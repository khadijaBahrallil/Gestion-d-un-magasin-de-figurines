package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    private Customer customer;
    private double subTotal;
    @OneToOne
    private PromoCodeClassic promoCodeClassic;
    @OneToOne
    private PromoCodePercent promoCodePercent;

    public Basket() {}

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
        return promoCodeClassic;
    }

    public void setClassicCode(PromoCodeClassic promoCodeClassic) {
        this.promoCodeClassic = promoCodeClassic;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PromoCodePercent getPromoCodePercent() {
        return promoCodePercent;
    }

    public void setPromoCodePercent(PromoCodePercent percentCode) {
        this.promoCodePercent = percentCode;
    }

}
