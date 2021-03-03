package com.example.demo.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class CollectionA {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @OneToMany
    private Collection<Figurine> figurines;
    @OneToOne
    private Customer customer;

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

}

