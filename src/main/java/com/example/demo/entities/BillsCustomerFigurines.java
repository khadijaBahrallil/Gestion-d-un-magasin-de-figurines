package com.example.demo.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(BillsCustomerFigurinesPK.class)
public class BillsCustomerFigurines implements Serializable {
    @Id
    @ManyToOne
    private BillsCustomer billsCustomer;

    @Id
    @ManyToOne
    private Figurine figurine;

    private Integer quantite;
    private double unitPrice;

    public BillsCustomer getBillsCustomer() {
        return billsCustomer;
    }

    public void setBillsCustomer(BillsCustomer billsCustomer) {
        this.billsCustomer = billsCustomer;
    }

    public Figurine getFigurine() {
        return figurine;
    }

    public void setFigurine(Figurine figurine) {
        this.figurine = figurine;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
