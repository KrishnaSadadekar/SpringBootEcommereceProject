package com.example.shopmax.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(

            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<Product> products;

    LocalDateTime dateTime;

    BigDecimal price;

    @ManyToOne
    @JoinColumn()
    private User user;
    @OneToOne
    private BillingAddress billingAddress;
    private String status;
    public Orders() {
    }

    public Orders(List<Product> products, LocalDateTime dateTime, BigDecimal price, User user, BillingAddress billingAddress, String status) {
        this.products = products;
        this.dateTime = dateTime;
        this.price = price;
        this.user = user;
        this.billingAddress = billingAddress;
        this.status = status;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
