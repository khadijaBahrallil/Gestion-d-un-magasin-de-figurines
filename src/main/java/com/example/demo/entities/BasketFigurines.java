package com.example.demo.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(BasketFigurinePK.class)
public class BasketFigurines implements Serializable {
    @Id
    @ManyToOne
    private Basket basket;

    @Id
    @ManyToOne
    private Figurine figurine;

    private Integer quantite;
    private double unitPrice;

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
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
