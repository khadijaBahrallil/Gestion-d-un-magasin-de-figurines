package com.example.demo.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Figurine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private Float price;
    private int quantity;
    @ManyToOne
    private Licence licence;
    @ManyToMany
    private Collection<Picture> pictures;
    @OneToMany
    private Collection<Basket> baskets;
    public enum Status {
        EN_PRECOMANDE,
        EN_STOCK,
        NON_DISPONIBLE,
        EN_COURS_DE_REAPPROVISIONNEMENT
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Licence getLicence() {
        return licence;
    }

    public void setLicence(Licence licence) {
        this.licence = licence;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Picture> getPictures() {
        return pictures;
    }
    public Picture getPicture() {
         Object[] mpictures =  pictures.toArray();
         Picture picture = (Picture) mpictures[0];
        return picture;
    }

    public void setPictures(Collection<Picture> pictures) {
        this.pictures = pictures;
    }

//A changer
    /*public void sortLicence(){

    }*/
}

