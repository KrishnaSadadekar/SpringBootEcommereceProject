package com.example.shopmax.models;

import jakarta.persistence.*;

@Entity
public class BillingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String city;
    private String Address1;
    private String Address2;
    private String state;
    private String zipcode;
    private String email;

    private String phoneNumber;

    @ManyToOne
    private  User user;

    public BillingAddress() {

    }

    public BillingAddress(String firstName, String lastName, String city, String address1, String address2, String state, String zipcode, String email, String phoneNumber, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        Address1 = address1;
        Address2 = address2;
        this.state = state;
        this.zipcode = zipcode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.user = user;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
