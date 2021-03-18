package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
public class Figurine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private Float price;
    private int quantity;
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date startDate;
    @ManyToOne
    private Licence licence;
    @ManyToMany
    private Collection<Picture> pictures;
    @OneToMany
    private Collection<Opinion> opinions;
    @Enumerated(EnumType.STRING)
    private StatusFigurine status;


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

    public void setQuantityBasket(int quantity) {
        this.quantity += quantity;
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

    public void setPicture(Picture picture) {
        pictures = new ArrayList<>();
        pictures.add(picture);
    }

    public void setPictures(Collection<Picture> pictures) {
        this.pictures = pictures;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Collection<Opinion> getOpinions() {
        return opinions;
    }

    public void setOpinions(Collection<Opinion> opinions) {
        this.opinions = opinions;
    }

    public void setOpinion(Opinion opinion) {
        this.opinions.add(opinion);
    }

    public StatusFigurine getStatus() {
        return status;
    }

    public void setStatus(StatusFigurine status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Figurine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", startDate=" + startDate +
                ", status=" + status +
                '}';
    }
}

