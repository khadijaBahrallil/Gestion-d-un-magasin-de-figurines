package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BillController {

    private BillController billController;

    @RequestMapping("/bill")
    public String bill(){ return "bill";}
}
