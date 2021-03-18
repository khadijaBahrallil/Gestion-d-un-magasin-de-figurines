package com.example.demo.entities;

import javax.persistence.*;
import java.sql.Date;
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
    private Date date;

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

    public StatusOrdered getStatus() {
        return status;
    }

    public void setStatus(StatusOrdered status) {
        this.status = status;
    }

    public PromoCodeClassic getPromoCodeClassic() {
        return promoCodeClassic;
    }

    public void setPromoCodeClassic(PromoCodeClassic promoCodeClassic) {
        this.promoCodeClassic = promoCodeClassic;
    }

    public PromoCodePercent getPromoCodePercent() {
        return promoCodePercent;
    }

    public void setPromoCodePercent(PromoCodePercent promoCodePercent) {
        this.promoCodePercent = promoCodePercent;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
