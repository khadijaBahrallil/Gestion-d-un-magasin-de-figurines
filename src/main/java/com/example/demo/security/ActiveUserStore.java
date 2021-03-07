package com.example.demo.security;

import java.util.ArrayList;
import java.util.List;

public class ActiveUserStore {

    public List<String> customers;

    public ActiveUserStore() {
        customers = new ArrayList<String>();
    }

    public List<String> getCustomers() {
        return customers;
    }

    public void setCustomers(List<String> customers) {
        this.customers = customers;
    }

}
