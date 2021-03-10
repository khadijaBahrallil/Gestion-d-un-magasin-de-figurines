package com.example.demo.security;

import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.springframework.stereotype.Component;

@Component
public class LoggedCustomer implements HttpSessionBindingListener {

    private String username;
    private ActiveUserStore activeUserStore;

    public LoggedCustomer(String username, ActiveUserStore activeUserStore) {
        this.username = username;
        this.activeUserStore = activeUserStore;
    }

    public LoggedCustomer() {
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        List<String> costumers = activeUserStore.getCustomers();
        LoggedCustomer customer = (LoggedCustomer) event.getValue();
        if (!costumers.contains(customer.getUsername())) {
            costumers.add(customer.getUsername());
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        List<String> customers = activeUserStore.getCustomers();
        LoggedCustomer customer = (LoggedCustomer) event.getValue();
        System.out.println(customer.getUsername());
        customers.remove(customer.getUsername());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
