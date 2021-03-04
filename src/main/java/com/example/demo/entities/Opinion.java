package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int note;
    private String text;
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date date;
    @OneToOne
    private Customer customer;
    @OneToOne
    private Figurine figurine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Figurine getFigurine() {
        return figurine;
    }

    public void setFigurine(Figurine figurine) {
        this.figurine = figurine;
    }
}
