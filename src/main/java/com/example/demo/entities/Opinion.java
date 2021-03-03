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
}
