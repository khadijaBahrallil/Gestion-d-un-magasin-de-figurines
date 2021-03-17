package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class BasketFigurines implements Serializable {
    @Id
    @ManyToOne
    private Basket basket;

    @Id
    @ManyToOne
    private Figurine figurine;

    private Integer quantite;
    private Float unitPrice;
    @OneToOne
    private PromoCodeClassic promoCodeClassic;
    @OneToOne
    private PromoCodePercent promoCodePercent;
}
