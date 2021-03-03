package com.example.demo.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Licence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @ManyToOne
    private Category category ;
    @OneToMany(mappedBy="licence")
    private Collection<Figurine> figurines;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Collection<Figurine> getFigurine() {
        return figurines;
    }

    public void setFigurine(Collection<Figurine> figurines) {
        this.figurines = figurines;
    }

    public Licence(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //A changer
    public void sortLicence(){

    }
}