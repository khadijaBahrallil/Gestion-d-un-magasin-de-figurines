package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class PromoCodeClassic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String code;
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date startDate;
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date endDate;
    private int nbUtilisation;
    private float reduction;
}
