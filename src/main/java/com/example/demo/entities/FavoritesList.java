package com.example.demo.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class FavoritesList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany
    private Collection<Figurine> figurines;
    @OneToOne
    private Customer customer;
}
