package com.example.demo.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Boolean civility;
    private int bonusPoint;
    private int assets;
    private String pseudo;
    private String phoneNumber;
    @OneToOne
    private Address address;
    @OneToOne
    private Address addressBills;
    @OneToOne
    private Address deliveryAddress;
    @OneToMany()
    private List<Address> adressCusto;
    @OneToMany
    private Collection<Opinion> opinions;
    @OneToOne
    private Subscription subscription;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCivility(Boolean civility) {
        if(civility){
            this.civility = true;
        }
        else{
            this.civility = false;
        }
    }

    public String getCivility() {
        if(civility){
            return "Homme";
        }
        return "Femme";
    }

    public Address getAddress(){
        return address;
    }

    public Address getAddressDelivery(){
        return deliveryAddress;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAddressDelivery(Address address) {
        this.deliveryAddress = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority("user"));

        return list;

    }

    public String getRole(){
        return "user";
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Collection<Opinion> getOpinions() {
        return opinions;
    }

    public void setOpinions(Collection<Opinion> opinions) {
        this.opinions = opinions;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
