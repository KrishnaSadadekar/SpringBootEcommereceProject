package com.example.shopmax.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Product product;

    private int quantity;

    private String imageUrl;

    private BigDecimal price;

    private BigDecimal amount;


    private String size;

    @ManyToOne
    private User user;


    public CartItem() {

    }

    public CartItem(Product product, int quantity, String imageUrl, BigDecimal price, BigDecimal amount, String size, User user) {
        this.product = product;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.price = price;
        this.amount = amount;
        this.size = size;
        this.user = user;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
