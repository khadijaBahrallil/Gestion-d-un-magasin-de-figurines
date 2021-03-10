package com.example.demo.security;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ActiveUserStore {

    public ArrayList<String> customers;

    public ActiveUserStore() {
        customers = new ArrayList<String>();
    }

    public List<String> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<String> customers) {
        this.customers = customers;
    }

}
